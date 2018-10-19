package com.example.annotation;

import java.util.Locale;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * Created by  on 2017/3/18
 * qq:1220289215
 */

public class FactoryAnnotateClass {
    private final String mId;
    private  String mSimpleTypeName;
    private TypeElement mAnnotatedTypeElement;
    private  String mQulifiedSuperClassName;

    public FactoryAnnotateClass(TypeElement typeElement) throws  IllegalArgumentException{
        mAnnotatedTypeElement = typeElement;
        Factory annotation = typeElement.getAnnotation(Factory.class);
        mId = annotation.id();
        if ( mId.length() == 0) {
            String warning = String.format(Locale.CHINA, "id() in @%s for %s is empty",
                    Factory.class.getSimpleName(), typeElement.getQualifiedName().toString());
            throw new IllegalArgumentException(warning);
        }
        try {
            Class<?> type = annotation.type();
            mQulifiedSuperClassName = type.getCanonicalName();
            mSimpleTypeName = type.getSimpleName();
        } catch (MirroredTypeException mte) {
            DeclaredType declaredType= (DeclaredType) mte.getTypeMirror();
            TypeElement typeElement1= (TypeElement) declaredType.asElement();
            mQulifiedSuperClassName = typeElement1.getQualifiedName().toString();
            mSimpleTypeName=typeElement1.getSimpleName().toString();
        }
    }

    public String getId() {
        return mId;
    }

    public String getSimpleTypeName() {
        return mSimpleTypeName;
    }

    public TypeElement getAnnotatedTypeElement() {
        return mAnnotatedTypeElement;
    }

    public String getQulifiedSuperClassName() {
        return mQulifiedSuperClassName;
    }
}
