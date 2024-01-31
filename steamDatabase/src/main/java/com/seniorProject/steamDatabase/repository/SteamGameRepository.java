package com.seniorProject.steamDatabase.repository;

import com.seniorProject.steamDatabase.model.GameInfo;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface SteamGameRepository extends ListCrudRepository<GameInfo, Integer> {

    List<GameInfo> findGameInfoByAppId(Integer appId);

    @Override
    <S extends GameInfo> List<S> saveAll(Iterable<S> entities);
}
