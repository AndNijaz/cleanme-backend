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
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

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
                .toList();
    }

    @Transactional
    public void removeFavourite(UUID clientId, UUID cleanerId) {
        System.out.println("Removing favorite: clientId=" + clientId + ", cleanerId=" + cleanerId);
        
        // Check if favorite exists first
        boolean exists = favouriteRepository.existsByClient_UidAndCleaner_Uid(clientId, cleanerId);
        if (!exists) {
            System.out.println("Favorite not found - may already be removed");
            return;
        }
        
        favouriteRepository.deleteByClient_UidAndCleaner_Uid(clientId, cleanerId);
        System.out.println("Favorite removed successfully");
    }
}
