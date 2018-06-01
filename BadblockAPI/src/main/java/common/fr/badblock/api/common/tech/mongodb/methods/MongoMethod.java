package fr.badblock.api.common.tech.mongodb.methods;

import fr.badblock.api.common.tech.mongodb.MongoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public abstract class MongoMethod 
{
	
	private MongoService mongoService;
	
	public abstract void run(MongoService mongoService2);
	
}
