package fr.badblock.api.common.sync.node;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ToengaInstanceStatus
{
	
	public List<Integer>	freePlaces;
	public int				totalPlaces;
	public int				placesPerGroup;
	public int				priority;
	
}
