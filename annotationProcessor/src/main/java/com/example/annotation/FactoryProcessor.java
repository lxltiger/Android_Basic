package com.example.annotation;

import com.google.auto.service.AutoService;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created by  on 2017/3/18
 * qq:1220289215
 */
@AutoService(Processor.class)
public class FactoryProcessor extends AbstractProcessor{

    private Types mTypeUtils;
    private Elements mElementUtils;
    private Filer mFiler;
    private Messager mMessager;
    private Map<String, FactoryGroupedClasses> mGroupedClassesMap = new LinkedHashMap<>();
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mTypeUtils = processingEnv.getTypeUtils();
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();

    }



    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Factory.class);
        for (Element element : elements) {
            System.out.println(element.getKind().toString());
            if (element.getKind() != ElementKind.CLASS) {
                    error(element,"only class can be annotated with %s",Factory.class.getSimpleName());
                return true;
            }
            TypeElement typeElement= (TypeElement) element;
            try {
                FactoryAnnotateClass factoryAnnotateClass=new FactoryAnnotateClass(typeElement);
                if (!isValidClass(factoryAnnotateClass)) {
                    return true;
                }
                FactoryGroupedClasses factoryGroupedClasses =
                        mGroupedClassesMap.get(factoryAnnotateClass.getQulifiedSuperClassName());
                if (factoryGroupedClasses == null) {
                    String qulifiedSuperClassName = factoryAnnotateClass.getQulifiedSuperClassName();
                    factoryGroupedClasses = new FactoryGroupedClasses(qulifiedSuperClassName);
                    mGroupedClassesMap.put(qulifiedSuperClassName, factoryGroupedClasses);
                }
                // Throws IdAlreadyUsedException if id is conflicting with
                // another @Factory annotated class with the same id
                factoryGroupedClasses.add(factoryAnnotateClass);
            } catch (IllegalArgumentException e) {
                //@factory id is empty
                error(element,e.getMessage());
                return true;
            }catch (IdAlreadyExistException  e){
                FactoryAnnotateClass existing = e.getExisting();
                error(typeElement,
                        "conflict the class %s is annotated with @%s with id='%s'which is already use the same id "
                ,typeElement.getQualifiedName().toString(),
                        Factory.class.getSimpleName(),existing.getAnnotatedTypeElement().getQualifiedName().toString());
                return true;
            }

        }

        try {
            for (FactoryGroupedClasses factoryGroupedClasses : mGroupedClassesMap.values()) {
                factoryGroupedClasses.generateCode(mElementUtils,mFiler);
            }
        } catch (Exception e) {
            error(null,e.getMessage());
        }
        return true;
    }

    private void error(Element element, String msg, Object...args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR,String.format(Locale.CHINA,msg,args),element);
    }

    private boolean isValidClass(FactoryAnnotateClass factoryAnnotateClass) {
        TypeElement typeElement = factoryAnnotateClass.getAnnotatedTypeElement();
        if (!typeElement.getModifiers().contains(Modifier.PUBLIC)) {
            error(typeElement,"the class %s is not public",typeElement.getQualifiedName().toString());
            return false;
        }
        if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
            error(typeElement,"the class %s id abstract,you can not annoated with %s",
                    typeElement.getQualifiedName().toString(),Factory.class.getSimpleName());
            return false;
        }

        // Check inheritance: Class must be childclass as specified in @Factory.type();
        TypeElement superClassElement =
                mElementUtils.getTypeElement(factoryAnnotateClass.getQulifiedSuperClassName());
        if (superClassElement.getKind() == ElementKind.INTERFACE) {
            // Check interface implemented
            if (!typeElement.getInterfaces().contains(superClassElement.asType())) {
                error(typeElement, "The class %s annotated with @%s must implement the interface %s",
                        typeElement.getQualifiedName().toString(), Factory.class.getSimpleName(),
                        factoryAnnotateClass.getQulifiedSuperClassName());
                return false;
            }
        } else {
            // Check subclassing
            TypeElement currentClass = typeElement;
            while (true) {
                TypeMirror superClassType = currentClass.getSuperclass();

                if (superClassType.getKind() == TypeKind.NONE) {
                    // Basis class (java.lang.Object) reached, so exit
                    error(typeElement, "The class %s annotated with @%s must inherit from %s",
                            typeElement.getQualifiedName().toString(), Factory.class.getSimpleName(),
                            factoryAnnotateClass.getQulifiedSuperClassName());
                    return false;
                }

                if (superClassType.toString().equals(factoryAnnotateClass.getQulifiedSuperClassName())) {
                    // Required super class found
                    break;
                }

                // Moving up in inheritance tree
                currentClass = (TypeElement) mTypeUtils.asElement(superClassType);
            }
        }

        // Check if an empty public constructor is given
        for (Element enclosed : typeElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructorElement = (ExecutableElement) enclosed;
                if (constructorElement.getParameters().size() == 0 && constructorElement.getModifiers()
                        .contains(Modifier.PUBLIC)) {
                    // Found an empty constructor
                    return true;
                }
            }
        }

        // No empty constructor found
        error(typeElement, "The class %s must provide an public empty default constructor",
                typeElement.getQualifiedName().toString());
        return false;

    }
}
