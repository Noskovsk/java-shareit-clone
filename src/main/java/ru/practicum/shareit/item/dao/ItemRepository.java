package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> getItemsByOwner(User owner);

    @Query("SELECT i FROM items i WHERE i.available IS TRUE AND (LOWER(i.name) LIKE %:searchString% " +
            "OR LOWER(i.description) LIKE %:searchString%)")
    List<Item> searchAllByString(String searchString);
}
