-- Copyright 2015 Boundary, Inc.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--    http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

local framework = require('framework')
local Plugin = framework.Plugin
local notEmpty = framework.string.notEmpty
local WebRequestDataSource = framework.WebRequestDataSource
local hasAny = framework.table.hasAny
local auth = framework.util.auth
local clone = framework.table.clone
local table = require('table')
local parseJson = framework.util.parseJson
local get = framework.table.get

local params = framework.params

params.pollInterval = notEmpty(params.pollInterval, 5000)
params.host = notEmpty(params.host, "localhost")
params.broker_name = notEmpty(params.broker_name, "localhost")
params.port = notEmpty(params.port, 8161)
params.source = notEmpty(params.source, params.broker_name)

local options = {}
options.host = params.host
options.port = params.port
options.auth = auth(params.username, params.password)
options.path = "/api/jolokia/read/org.apache.activemq:type=Broker,brokerName=" .. params.broker_name
options.wait_for_end = false
options.debug_level = params.debug_level

local function childDataSource(object)
    local opts = clone(options)
    opts.path = "/api/jolokia/read/" .. object
    opts.meta = object
    return WebRequestDataSource:new(opts)
end

local pending_requests = {}
local plugin
local ds = WebRequestDataSource:new(options)
ds:chain(function(context, callback, data)
    local success, parsed = parseJson(data)
    if not success then
        context:emit('error', 'Can not parse metrics. Verify configuration parameters.')
        return
    end
    parsed = get('value', parsed)
    local metrics = {
        ['ACTIVEMQ_BROKER_TOTALS_QUEUES'] = parsed.Queues and #parsed.Queues,
        ['ACTIVEMQ_BROKER_TOTALS_TOPICS'] = parsed.Topics and #parsed.Topics,
        ['ACTIVEMQ_BROKER_TOTALS_PRODUCERS'] = parsed.TotalProducerCount,
        ['ACTIVEMQ_BROKER_TOTALS_CONSUMERS'] = parsed.TotalConsumerCount,
        ['ACTIVEMQ_BROKER_TOTALS_MESSAGES'] = parsed.TotalMessageCount,
        ['ACTIVEMQ_MEM_USED'] = parsed.MemoryPercentUsage,
        ['ACTIVEMQ_STORE_USED'] = parsed.StorePercentUsage/100.0,
    }
    plugin:report(metrics)

    local data_sources = {}
    for _, v in ipairs(parsed.Topics) do
        m = string.match(v.objectName, 'ActiveMQ.Advisory')
        if m == nil then
            local child_ds = childDataSource(v.objectName)
            child_ds:propagate('error', context)
            table.insert(data_sources, child_ds)
            pending_requests[v.objectName] = true
        end
    end
    for _, v in ipairs(parsed.Queues) do
        m = string.match(v.objectName, 'ActiveMQ.Advisory')
        if m == nil then
            local child_ds = childDataSource(v.objectName)
            child_ds:propagate('error', context)
            table.insert(data_sources, child_ds)
            pending_requests[v.objectName] = true
        end
    end
    return data_sources
end)

local stats_total_tmpl = {
    EnqueueCount = 0,
    DequeueCount = 0,
    InFlightCount = 0,
    DispatchCount = 0,
    ExpiredCount = 0,
    QueueSize = 0
}

local stats_total = clone(stats_total_tmpl)

local function component_names(line)
    local words = {}
    local i = 1
    for token in string.gmatch(line, "[^,]+") do
        _, words[i] = string.match(token, "(%g+)=(%g+)")
        i = i + 1
    end
    return words[1], words[2], words[3], words[4]
end


plugin = Plugin:new(params, ds)
function plugin:onParseValues(data, extra)
    local success, parsed = parseJson(data)
    if not success then
        self:error('Can not parse metrics. Verify configuration parameters.')
        return
    end

    ts = parsed.timestamp
    parsed = get('value', parsed)

    local metrics = {}

    local broker_name, destination_name, destination_type, _type = component_names(extra.info)
    local source_name = broker_name .. '-' .. destination_type .. '-' .. destination_name

    if _type == 'Queue' then
	metrics['ACTIVEMQ_TOPIC_PRODUCERS'] = {}
	metrics['ACTIVEMQ_TOPIC_CONSUMERS'] = {}
        metrics['ACTIVEMQ_TOPIC_ENQUEUE'] = {}
        metrics['ACTIVEMQ_TOPIC_DEQUEUE'] = {}
        metrics['ACTIVEMQ_TOPIC_INFLIGHT'] = {}
        metrics['ACTIVEMQ_TOPIC_DISPATCH'] = {}
        metrics['ACTIVEMQ_TOPIC_EXPIRED'] = {}
        metrics['ACTIVEMQ_TOPIC_SIZE'] = {}
        table.insert(metrics['ACTIVEMQ_TOPIC_PRODUCERS'], { value = parsed.ProducerCount, source = source_name, timestamp = ts})
        table.insert(metrics['ACTIVEMQ_TOPIC_CONSUMERS'], { value = parsed.ConsumerCount, source = source_name, timestamp = ts})
        table.insert(metrics['ACTIVEMQ_TOPIC_ENQUEUE'], { value = parsed.EnqueueCount, source = source_name, timestamp = ts})
        table.insert(metrics['ACTIVEMQ_TOPIC_DEQUEUE'], { value = parsed.DequeueCount, source = source_name, timestamp = ts})
        table.insert(metrics['ACTIVEMQ_TOPIC_INFLIGHT'], { value =parsed.InFlightCount, source = source_name, timestamp = ts })
        table.insert(metrics['ACTIVEMQ_TOPIC_DISPATCH'], { value =parsed.DispatchCount, source = source_name, timestamp = ts })
        table.insert(metrics['ACTIVEMQ_TOPIC_EXPIRED'], { value = parsed.ExpiredCount, source = source_name, timestamp = ts })
        table.insert(metrics['ACTIVEMQ_TOPIC_SIZE'], { value = parsed.QueueSize, source = source_name, timestamp = ts })
    else
	metrics['ACTIVEMQ_QUEUE_PRODUCERS'] = {}
	metrics['ACTIVEMQ_QUEUE_CONSUMERS'] = {}
        metrics['ACTIVEMQ_QUEUE_ENQUEUE'] = {}
        metrics['ACTIVEMQ_QUEUE_DEQUEUE'] = {}
        metrics['ACTIVEMQ_QUEUE_INFLIGHT'] = {}
        metrics['ACTIVEMQ_QUEUE_DISPATCH'] = {}
        metrics['ACTIVEMQ_QUEUE_EXPIRED'] = {}
        metrics['ACTIVEMQ_QUEUE_SIZE'] = {}
        table.insert(metrics['ACTIVEMQ_QUEUE_PRODUCERS'], { value = parsed.ProducerCount, source = source_name, timestamp = ts})
        table.insert(metrics['ACTIVEMQ_QUEUE_CONSUMERS'], { value = parsed.ConsumerCount, source = source_name, timestamp = ts})
        table.insert(metrics['ACTIVEMQ_QUEUE_ENQUEUE'], { value = parsed.EnqueueCount, source = source_name, timestamp = ts })
        table.insert(metrics['ACTIVEMQ_QUEUE_DEQUEUE'], { value = parsed.DequeueCount, source = source_name, timestamp = ts })
        table.insert(metrics['ACTIVEMQ_QUEUE_INFLIGHT'], { value =parsed.InFlightCount, source = source_name, timestamp = ts })
        table.insert(metrics['ACTIVEMQ_QUEUE_DISPATCH'], { value =parsed.DispatchCount, source = source_name, timestamp = ts })
        table.insert(metrics['ACTIVEMQ_QUEUE_EXPIRED'], { value = parsed.ExpiredCount, source = source_name, timestamp = ts })
        table.insert(metrics['ACTIVEMQ_QUEUE_SIZE'], { value = parsed.QueueSize, source = source_name, timestamp = ts })
    end

    return metrics
end

plugin:run()

