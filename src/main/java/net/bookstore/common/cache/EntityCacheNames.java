package net.bookstore.common.cache;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public abstract class EntityCacheNames {


    public static final String ROLE = "role";
    public static final String ALL_ROLES = "allRoles";
    public static final String GENRE = "genre";
    public static final String ALL_GENRES = "allGenres";
    public static final String AUTHOR = "author";
    public static final String ALL_AUTHORS = "allAuthors";
    public static final String BOOK = "book";
    public static final String ALL_BOOKS = "allBooks";

    public static final String SYSTEM_USER = "systemUser";
    public static final String ALL_SYSTEM_USERS = "allSystemUsers";

    public static Set<String> getCacheNames() throws IllegalAccessException {

        Set<String> cacheNames = new HashSet<>();

        // Get fields via reflection
        Field[] fields = EntityCacheNames.class.getFields();
        // Get value for each field
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(null);
            cacheNames.add(value.toString());
        }
        return cacheNames;
    }

}
