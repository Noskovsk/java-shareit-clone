package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> getItemsByOwner(User owner);

    @Query("SELECT i FROM items i WHERE LOWER(i.name) like %:searchString%")
    List<Item> searchItemsByString(String searchString);
}
