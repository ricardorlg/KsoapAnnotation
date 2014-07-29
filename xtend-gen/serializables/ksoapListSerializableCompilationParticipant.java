package serializables;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.AnnotationTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy;
import org.eclipse.xtend.lib.macro.declaration.InterfaceDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableConstructorDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.ParameterDeclaration;
import org.eclipse.xtend.lib.macro.declaration.Type;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import serializables.KsoapListSerializable;
import serializables.KsoapSerializable;

@SuppressWarnings("all")
public class ksoapListSerializableCompilationParticipant extends AbstractClassProcessor {
  public void doTransform(final List<? extends MutableClassDeclaration> annotatedClasses, @Extension final TransformationContext context) {
    final Procedure1<MutableClassDeclaration> _function = new Procedure1<MutableClassDeclaration>() {
      public void apply(final MutableClassDeclaration it) {
        ksoapListSerializableCompilationParticipant.this.doTransform(it, context);
      }
    };
    IterableExtensions.forEach(annotatedClasses, _function);
  }
  
  public void doTransform(final MutableClassDeclaration clazz, @Extension final TransformationContext context) {
    TypeReference _extendedClass = clazz.getExtendedClass();
    TypeReference _newTypeReference = context.newTypeReference(Object.class);
    boolean _equals = Objects.equal(_extendedClass, _newTypeReference);
    if (_equals) {
      Iterable<? extends AnnotationReference> _annotations = clazz.getAnnotations();
      AnnotationReference _head = IterableExtensions.head(_annotations);
      context.addError(_head, "Debes extender de Vector");
    }
    TypeReference _extendedClass_1 = clazz.getExtendedClass();
    List<TypeReference> _actualTypeArguments = _extendedClass_1.getActualTypeArguments();
    final TypeReference tipo = _actualTypeArguments.get(0);
    Type _type = tipo.getType();
    final ClassDeclaration tipo2 = ((ClassDeclaration) _type);
    final String parameterName = this.getValue(clazz, context);
    final TypeReference interfaceUsed = context.newTypeReference(KvmSerializable.class);
    final TypeReference serializable = context.newTypeReference(Serializable.class);
    Iterable<? extends TypeReference> _implementedInterfaces = clazz.getImplementedInterfaces();
    Iterable<TypeReference> _plus = Iterables.<TypeReference>concat(_implementedInterfaces, Collections.<TypeReference>unmodifiableList(Lists.<TypeReference>newArrayList(interfaceUsed, serializable)));
    clazz.setImplementedInterfaces(_plus);
    final Procedure1<MutableConstructorDeclaration> _function = new Procedure1<MutableConstructorDeclaration>() {
      public void apply(final MutableConstructorDeclaration it) {
        TypeReference _newTypeReference = context.newTypeReference(SoapObject.class);
        it.addParameter("object", _newTypeReference);
        final CompilationStrategy _function = new CompilationStrategy() {
          public CharSequence compile(final CompilationStrategy.CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("int size = object.getPropertyCount();");
            _builder.newLine();
            _builder.append(" ");
            _builder.append("for (int i0=0;i0< size;i0++)");
            _builder.newLine();
            _builder.append("       ");
            _builder.append("{");
            _builder.newLine();
            _builder.append("           ");
            _builder.append("Object obj = object.getProperty(i0);");
            _builder.newLine();
            {
              TypeReference _newTypeReference = context.newTypeReference(KsoapSerializable.class);
              Type _type = _newTypeReference.getType();
              AnnotationReference _findAnnotation = tipo2.findAnnotation(_type);
              boolean _notEquals = (!Objects.equal(_findAnnotation, null));
              if (_notEquals) {
                _builder.append("           ");
                _builder.append("if (obj!=null && obj instanceof ");
                TypeReference _newTypeReference_1 = context.newTypeReference(SoapObject.class);
                String _javaCode = it.toJavaCode(_newTypeReference_1);
                _builder.append(_javaCode, "           ");
                _builder.append(")");
                _builder.newLineIfNotEmpty();
                _builder.append("           ");
                _builder.append("{");
                _builder.newLine();
                _builder.append("           ");
                _builder.append("SoapObject j =(SoapObject) object.getProperty(i0);");
                _builder.newLine();
                _builder.append("           ");
                String _javaCode_1 = it.toJavaCode(tipo);
                _builder.append(_javaCode_1, "           ");
                _builder.append(" j1= new ");
                String _simpleName = tipo.getSimpleName();
                _builder.append(_simpleName, "           ");
                _builder.append("(j);\t");
                _builder.newLineIfNotEmpty();
                _builder.append("           ");
                _builder.append("add(j1);");
                _builder.newLine();
                _builder.append("           ");
                _builder.newLine();
                _builder.append("           ");
                _builder.append("}");
                _builder.newLine();
              } else {
                _builder.append("           ");
                _builder.append("if (obj!=null && obj instanceof ");
                TypeReference _newTypeReference_2 = context.newTypeReference(SoapPrimitive.class);
                String _javaCode_2 = it.toJavaCode(_newTypeReference_2);
                _builder.append(_javaCode_2, "           ");
                _builder.append(")");
                _builder.newLineIfNotEmpty();
                _builder.append("           ");
                _builder.append("{");
                _builder.newLine();
                _builder.append("           ");
                _builder.append("SoapPrimitive j =(SoapPrimitive) object.getProperty(i0);\t\t\t\t\t           \t ");
                _builder.newLine();
                _builder.append("           ");
                _builder.append("  \t");
                String _javaCode_3 = it.toJavaCode(tipo);
                _builder.append(_javaCode_3, "             \t");
                _builder.append(" j1= ");
                String _typeConverted = ksoapListSerializableCompilationParticipant.this.typeConverted(tipo, "j");
                _builder.append(_typeConverted, "             \t");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("           ");
                _builder.append("add(j1);");
                _builder.newLine();
                _builder.append("           ");
                _builder.append("}");
                _builder.newLine();
              }
            }
            _builder.newLine();
            _builder.append("       ");
            _builder.append("}");
            _builder.newLine();
            return _builder;
          }
        };
        it.setBody(_function);
      }
    };
    clazz.addConstructor(_function);
    Type _type_1 = interfaceUsed.getType();
    final InterfaceDeclaration s = ((InterfaceDeclaration) _type_1);
    Iterable<? extends MethodDeclaration> _declaredMethods = s.getDeclaredMethods();
    for (final MethodDeclaration method : _declaredMethods) {
      String _simpleName = method.getSimpleName();
      boolean _equalsIgnoreCase = _simpleName.equalsIgnoreCase("getPropertyCount");
      if (_equalsIgnoreCase) {
        String _simpleName_1 = method.getSimpleName();
        final Procedure1<MutableMethodDeclaration> _function_1 = new Procedure1<MutableMethodDeclaration>() {
          public void apply(final MutableMethodDeclaration it) {
            Iterable<? extends ParameterDeclaration> _parameters = method.getParameters();
            for (final ParameterDeclaration p : _parameters) {
              String _simpleName = p.getSimpleName();
              TypeReference _type = p.getType();
              it.addParameter(_simpleName, _type);
            }
            TypeReference _returnType = method.getReturnType();
            it.setReturnType(_returnType);
            final CompilationStrategy _function = new CompilationStrategy() {
              public CharSequence compile(final CompilationStrategy.CompilationContext it) {
                StringConcatenation _builder = new StringConcatenation();
                _builder.append("return this.size();");
                _builder.newLine();
                return _builder;
              }
            };
            it.setBody(_function);
          }
        };
        clazz.addMethod(_simpleName_1, _function_1);
      } else {
        String _simpleName_2 = method.getSimpleName();
        boolean _equalsIgnoreCase_1 = _simpleName_2.equalsIgnoreCase("getProperty");
        if (_equalsIgnoreCase_1) {
          String _simpleName_3 = method.getSimpleName();
          final Procedure1<MutableMethodDeclaration> _function_2 = new Procedure1<MutableMethodDeclaration>() {
            public void apply(final MutableMethodDeclaration it) {
              Iterable<? extends ParameterDeclaration> _parameters = method.getParameters();
              for (final ParameterDeclaration p : _parameters) {
                TypeReference _type = p.getType();
                it.addParameter("index", _type);
              }
              TypeReference _returnType = method.getReturnType();
              it.setReturnType(_returnType);
              final CompilationStrategy _function = new CompilationStrategy() {
                public CharSequence compile(final CompilationStrategy.CompilationContext it) {
                  StringConcatenation _builder = new StringConcatenation();
                  _builder.append("return this.get(index);");
                  _builder.newLine();
                  return _builder;
                }
              };
              it.setBody(_function);
            }
          };
          clazz.addMethod(_simpleName_3, _function_2);
        } else {
          String _simpleName_4 = method.getSimpleName();
          boolean _equalsIgnoreCase_2 = _simpleName_4.equalsIgnoreCase("getPropertyInfo");
          if (_equalsIgnoreCase_2) {
            String _simpleName_5 = method.getSimpleName();
            final Procedure1<MutableMethodDeclaration> _function_3 = new Procedure1<MutableMethodDeclaration>() {
              public void apply(final MutableMethodDeclaration it) {
                Iterable<? extends ParameterDeclaration> _parameters = method.getParameters();
                for (final ParameterDeclaration p : _parameters) {
                  String _simpleName = p.getSimpleName();
                  TypeReference _type = p.getType();
                  it.addParameter(_simpleName, _type);
                }
                TypeReference _returnType = method.getReturnType();
                it.setReturnType(_returnType);
                final CompilationStrategy _function = new CompilationStrategy() {
                  public CharSequence compile(final CompilationStrategy.CompilationContext it) {
                    StringConcatenation _builder = new StringConcatenation();
                    _builder.append("arg2.name=\"");
                    _builder.append(parameterName, "");
                    _builder.append("\";");
                    _builder.newLineIfNotEmpty();
                    _builder.append("arg2.type=");
                    String _javaCode = it.toJavaCode(tipo);
                    _builder.append(_javaCode, "");
                    _builder.append(".class;");
                    _builder.newLineIfNotEmpty();
                    return _builder;
                  }
                };
                it.setBody(_function);
              }
            };
            clazz.addMethod(_simpleName_5, _function_3);
          } else {
            String _simpleName_6 = method.getSimpleName();
            final Procedure1<MutableMethodDeclaration> _function_4 = new Procedure1<MutableMethodDeclaration>() {
              public void apply(final MutableMethodDeclaration it) {
                Iterable<? extends ParameterDeclaration> _parameters = method.getParameters();
                for (final ParameterDeclaration p : _parameters) {
                  String _simpleName = p.getSimpleName();
                  TypeReference _type = p.getType();
                  it.addParameter(_simpleName, _type);
                }
                TypeReference _returnType = method.getReturnType();
                it.setReturnType(_returnType);
                final CompilationStrategy _function = new CompilationStrategy() {
                  public CharSequence compile(final CompilationStrategy.CompilationContext it) {
                    StringConcatenation _builder = new StringConcatenation();
                    _builder.append("this.add(");
                    String _typeConverted = ksoapListSerializableCompilationParticipant.this.typeConverted(tipo, "arg1");
                    _builder.append(_typeConverted, "");
                    _builder.append(");");
                    _builder.newLineIfNotEmpty();
                    return _builder;
                  }
                };
                it.setBody(_function);
              }
            };
            clazz.addMethod(_simpleName_6, _function_4);
          }
        }
      }
    }
  }
  
  public String typeConverted(final TypeReference reference, final String paramName) {
    String _switchResult = null;
    String _simpleName = reference.getSimpleName();
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_simpleName, "Boolean")) {
        _matched=true;
        _switchResult = (("Boolean.parseBoolean(" + paramName) + ".toString())");
      }
    }
    if (!_matched) {
      if (Objects.equal(_simpleName, "Long")) {
        _matched=true;
        _switchResult = (("Long.parseLong(" + paramName) + ".toString())");
      }
    }
    if (!_matched) {
      if (Objects.equal(_simpleName, "Integer")) {
        _matched=true;
        _switchResult = (("Integer.parseInt(" + paramName) + ".toString())");
      }
    }
    if (!_matched) {
      if (Objects.equal(_simpleName, "String")) {
        _matched=true;
        _switchResult = (paramName + ".toString()");
      }
    }
    if (!_matched) {
      if (Objects.equal(_simpleName, "Float")) {
        _matched=true;
        _switchResult = (("Float.parseFloat(" + paramName) + ".toString())");
      }
    }
    if (!_matched) {
      if (Objects.equal(_simpleName, "Double")) {
        _matched=true;
        _switchResult = (("Double.parseDouble(" + paramName) + ".toString())");
      }
    }
    if (!_matched) {
      if (Objects.equal(_simpleName, "Date")) {
        _matched=true;
        _switchResult = (("utils.DatesUtils.parses(" + paramName) + ".toString())");
      }
    }
    if (!_matched) {
      if (Objects.equal(_simpleName, "Character")) {
        _matched=true;
        _switchResult = (paramName + ".toString().charAt(0)");
      }
    }
    if (!_matched) {
      if (Objects.equal(_simpleName, "byte[]")) {
        _matched=true;
        _switchResult = (("org.kobjects.base64.Base64.decode(" + paramName) + ".toString())");
      }
    }
    if (!_matched) {
      String _simpleName_1 = reference.getSimpleName();
      String _plus = ("(" + _simpleName_1);
      String _plus_1 = (_plus + ")");
      _switchResult = (_plus_1 + paramName);
    }
    return _switchResult;
  }
  
  public String getValue(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context) {
    Iterable<? extends AnnotationReference> _annotations = annotatedClass.getAnnotations();
    final Function1<AnnotationReference, Boolean> _function = new Function1<AnnotationReference, Boolean>() {
      public Boolean apply(final AnnotationReference it) {
        AnnotationTypeDeclaration _annotationTypeDeclaration = it.getAnnotationTypeDeclaration();
        TypeReference _newTypeReference = context.newTypeReference(KsoapListSerializable.class);
        Type _type = _newTypeReference.getType();
        return Boolean.valueOf(Objects.equal(_annotationTypeDeclaration, _type));
      }
    };
    AnnotationReference _findFirst = IterableExtensions.findFirst(_annotations, _function);
    final Object value = _findFirst.getValue("nombre");
    boolean _equals = Objects.equal(value, null);
    if (_equals) {
      return null;
    }
    return value.toString();
  }
}
