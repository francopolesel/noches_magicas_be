//Ejecutar estos comandos para poder usar sharding

// docker exec -it router01 bash
// mongosh
// use nm
// db.show.ensureIndex({"_id": "hashed"})
// sh.enableSharding("nm")
// sh.shardCollection("nm.show", {"_id": "hashed" }, false)
