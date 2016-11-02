package com.github.jakubkolar.autobuilder.impl;

import com.github.jakubkolar.autobuilder.spi.ValueResolver;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

class VRContext<T> implements ValueResolver.Context<T> {

    private final ValueResolver rootResolver;
    private final VRContext<?> parent;
    private final String name;
    private final Class<T> type;
    private final Optional<Type> typeInfo;
    private final ImmutableCollection<Annotation> annotations;

    private T result = null;
    private Optional<Exception> error = Optional.empty();
    private boolean resolved = false;

    public static <T> VRContext<T> createRoot(ValueResolver rootResolver, Class<T> type) {
        return new VRContext<>(
                rootResolver,
                null,
                type.getSimpleName(),
                type,
                Optional.empty(),
                ImmutableList.of()
        );
    }

    public VRContext<?> createChild(Field field) {
        return new VRContext<>(
                rootResolver,
                this,
                field.getName(),
                field.getType(),
                Optional.of(field.getGenericType()),
                ImmutableList.copyOf(field.getAnnotations())
        );
    }

    private VRContext(ValueResolver rootResolver, VRContext<?> parent, String name, Class<T> type, Optional<Type> typeInfo, ImmutableCollection<Annotation> annotations) {
        this.rootResolver = rootResolver;
        this.parent = parent;
        this.name = name;
        this.type = type;
        this.typeInfo = typeInfo;
        this.annotations = annotations;
    }

    @Override
    public ValueResolver getRootResolver() {
        return rootResolver;
    }

    @Override
    public Optional<ValueResolver.Context<?>> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public String getName() {
        return parent == null ? name : parent.getName() + '.' + name;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public Optional<Type> getTypeInfo() {
        return typeInfo;
    }

    @Override
    public Collection<Annotation> getAnnotations() {
        return annotations;
    }

    @Override
    public T getResult() {
        return result;
    }

    @Override
    public void setResult(Object result) {
        this.result = type.cast(result);
    }

    @Override
    public Optional<Exception> getError() {
        return error;
    }

    @Override
    public void setResolved() {
        this.resolved = true;
    }

    @Override
    public void setFailed(Exception e) {
        this.error = Optional.of(e);
    }

    // TODO: toString for debugging?
}
