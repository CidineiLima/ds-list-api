package com.devcid.dslist.services;
import com.devcid.dslist.dto.GameListDTO;
import com.devcid.dslist.entities.GameList;
import com.devcid.dslist.projections.GameMinProjection;
import com.devcid.dslist.repositories.GameListRepository;
import com.devcid.dslist.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameListService {

	@Autowired
	private GameListRepository gameListRepository;

	@Autowired
	private GameRepository gameRepository;

	@Transactional(readOnly = true)
	public List<GameListDTO> findAll(){
		List<GameList> result = gameListRepository.findAll();
		return result.stream().map(x -> new GameListDTO(x)).toList();
	}

	@Transactional
	public void move(Long listId, int sourceIndex, int destinationIndex) {

		List<GameMinProjection> list = gameRepository.searchByList(listId);

		GameMinProjection obj = list.remove(sourceIndex);
		list.add(destinationIndex, obj);

		int min = sourceIndex < destinationIndex ? sourceIndex : destinationIndex;
		int max = sourceIndex < destinationIndex ? destinationIndex : sourceIndex;

		for (int i = min; i <= max; i++) {
			gameListRepository.updateBelongingPosition(listId, list.get(i).getId(), i);
		}
	}

	@Transactional(readOnly = true)
	public GameListDTO findById(Long id) {
		GameList entity = gameListRepository.findById(id).get();
		return new GameListDTO(entity);
	}
}
