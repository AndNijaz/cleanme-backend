package com.cleanme.controller;

import com.cleanme.dto.AddFavouriteRequest;
import com.cleanme.dto.FavouriteDto;
import com.cleanme.service.FavouriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/favourites")
@RequiredArgsConstructor
public class FavouriteController {

    private final FavouriteService favouriteService;

    @PostMapping
    public ResponseEntity<Void> addFavourite(@RequestBody @Valid AddFavouriteRequest request) {
        favouriteService.addFavourite(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<List<FavouriteDto>> getFavourites(@PathVariable UUID clientId) {
        return ResponseEntity.ok(favouriteService.getFavourites(clientId));
    }

    @DeleteMapping("/{clientId}/{cleanerId}")
    public ResponseEntity<Void> removeFavourite(@PathVariable UUID clientId, @PathVariable UUID cleanerId) {
        favouriteService.removeFavourite(clientId, cleanerId);
        return ResponseEntity.ok().build();
    }
}
