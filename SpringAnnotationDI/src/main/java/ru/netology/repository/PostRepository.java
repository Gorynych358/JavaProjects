package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository implements IPostRepository {
    private final ConcurrentHashMap<Long, Post> postStorage = new ConcurrentHashMap<>();
    private final AtomicLong postId = new AtomicLong(0);

    public List<Post> all() {
        if (!postStorage.isEmpty()) //Возвращаем все имеющиеся посты:
            return new ArrayList<>(postStorage.values());
        else
            throw new NullPointerException("There isn't post in repository!");
    }

    public Optional<Post> getById(long id) //Возвращаем пост по ID если есть:
    {
        return Optional.ofNullable(postStorage.get(id));

    }

    public Post save(Post post) throws NotFoundException {

        if (post.getId() == 0) //ID поста равен нулю. Сохраняем новый пост:
        {
            post.setId(postId.get());
            postId.getAndIncrement();
            postStorage.put(post.getId(), post);
        }
        else if (postStorage.containsKey(post.getId())) //Уже есть пост с таким ID, обновляем его:
        {
            postStorage.put(post.getId(), post);
        }
        else
            throw new NotFoundException("The post doesn't found");

        return post;
    }

    public void removeById(long id) throws NotFoundException
    {
        if (postStorage.containsKey(id))
        {
            postStorage.remove(id);
        }
        else
            throw new NotFoundException("The post doesn't found");
    }
}
