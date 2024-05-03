package net.bookstore.service.entity;

import net.bookstore.entity.templates.AbstractPersistentObject;

import java.util.List;

public interface EntityReadService<T extends AbstractPersistentObject> {

    List<T> getAllEntities();

    T getEntity(Long entityId);

}
