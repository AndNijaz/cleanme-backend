package com.cleanme.service;

import com.cleanme.dto.AddFavouriteRequest;
import com.cleanme.dto.FavouriteDto;
import com.cleanme.entity.FavouriteEntity;
import com.cleanme.entity.UsersEntity;
import com.cleanme.enums.UserType;
import com.cleanme.repository.FavouriteRepository;
import com.cleanme.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavouriteService {

    private final FavouriteRepository favouriteRepository;
    private final UsersRepository usersRepository;

    public void addFavourite(AddFavouriteRequest request) {
        UUID clientId = request.getClientId();
        UUID cleanerId = request.getCleanerId();

        if (favouriteRepository.existsByClient_UidAndCleaner_Uid(clientId, cleanerId)) {
            throw new RuntimeException("Already in favourites.");
        }

        UsersEntity client = usersRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found."));

        UsersEntity cleaner = usersRepository.findById(cleanerId)
                .orElseThrow(() -> new RuntimeException("Cleaner not found."));

        if (cleaner.getUserType() != UserType.CLEANER) {
            throw new RuntimeException("Selected user is not a cleaner.");
        }

        FavouriteEntity favourite = new FavouriteEntity();
        favourite.setClient(client);
        favourite.setCleaner(cleaner);

        favouriteRepository.save(favourite);
    }

    public List<FavouriteDto> getFavourites(UUID clientId) {
        return favouriteRepository.findByClient_Uid(clientId)
                .stream()
                .map(fav -> new FavouriteDto(
                        fav.getCleaner().getUid(),
                        fav.getCleaner().getFirstName() + " " + fav.getCleaner().getLastName()
                ))
                .collect(Collectors.toList());
    }

    public void removeFavourite(UUID clientId, UUID cleanerId) {
        favouriteRepository.deleteByClient_UidAndCleaner_Uid(clientId, cleanerId);
    }
}
