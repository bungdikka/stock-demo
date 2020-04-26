-- checkandset.lua
local current = redis.call('GET', KEYS[1])
local currentNum = tonumber(current);
local argvNum = tonumber(ARGV[1]);
if currentNum > 0 and (currentNum-argvNum)>=0
  then redis.call('DECRBY', KEYS[1], ARGV[1])
  return true
end
return false