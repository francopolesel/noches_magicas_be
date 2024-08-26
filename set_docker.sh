#!/bin/bash
# Ejecutar los comandos de Docker Compose
 docker-compose -f docker-sharding.yml exec configsvr01 sh -c "mongosh < /scripts/init-configserver.js"
 docker-compose -f docker-sharding.yml exec shard01-a sh -c "mongosh < /scripts/init-shard01.js"
 docker-compose -f docker-sharding.yml exec shard02-a sh -c "mongosh < /scripts/init-shard02.js"
 sleep 30
 docker-compose -f docker-sharding.yml exec router01 sh -c "mongosh < /scripts/init-router.js"


# Ejecutar comandos en router01
sleep 5
docker-compose -f docker-sharding.yml exec router01 mongosh --eval '
db.shows.ensureIndex({"_id": "hashed"})
sh.enableSharding("nm")
sh.shardCollection("nm.shows", {"_id": "hashed"}, false)
'