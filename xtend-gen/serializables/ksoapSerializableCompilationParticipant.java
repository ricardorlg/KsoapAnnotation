package serializables;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.RegisterGlobalsContext;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy;
import org.eclipse.xtend.lib.macro.declaration.InterfaceDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableConstructorDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.ParameterDeclaration;
import org.eclipse.xtend.lib.macro.declaration.Type;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import serializables.KsoapObject;

@SuppressWarnings("all")
public class ksoapSerializableCompilationParticipant extends AbstractClassProcessor {
  public void doRegisterGlobals(final ClassDeclaration annotatedClass, @Extension final RegisterGlobalsContext context) {
  }
  
  public void doTransform(final List<? extends MutableClassDeclaration> annotatedClasses, @Extension final TransformationContext context) {
    final Procedure1<MutableClassDeclaration> _function = new Procedure1<MutableClassDeclaration>() {
      public void apply(final MutableClassDeclaration it) {
        ksoapSerializableCompilationParticipant.this.doTransform(it, context);
      }
    };
    IterableExtensions.forEach(annotatedClasses, _function);
  }
  
  public void doTransform(final MutableClassDeclaration clazz, @Extension final TransformationContext context) {
    final TypeReference interfaceUsed = context.newTypeReference(KvmSerializable.class);
    final TypeReference serializable = context.newTypeReference(Serializable.class);
    Iterable<? extends TypeReference> _implementedInterfaces = clazz.getImplementedInterfaces();
    Iterable<TypeReference> _plus = Iterables.<TypeReference>concat(_implementedInterfaces, Collections.<TypeReference>unmodifiableList(Lists.<TypeReference>newArrayList(interfaceUsed, serializable)));
    clazz.setImplementedInterfaces(_plus);
    this.addConstructor(clazz, context);
    Type _type = interfaceUsed.getType();
    final InterfaceDeclaration s = ((InterfaceDeclaration) _type);
    Iterable<? extends MethodDeclaration> _declaredMethods = s.getDeclaredMethods();
    for (final MethodDeclaration method : _declaredMethods) {
      String _simpleName = method.getSimpleName();
      boolean _equalsIgnoreCase = _simpleName.equalsIgnoreCase("getPropertyCount");
      if (_equalsIgnoreCase) {
        String _simpleName_1 = method.getSimpleName();
        final Procedure1<MutableMethodDeclaration> _function = new Procedure1<MutableMethodDeclaration>() {
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
                {
                  Iterable<? extends MutableFieldDeclaration> _declaredFields = clazz.getDeclaredFields();
                  boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(_declaredFields);
                  if (_isNullOrEmpty) {
                    _builder.append("return 0;");
                    _builder.newLine();
                  } else {
                    {
                      TypeReference _extendedClass = clazz.getExtendedClass();
                      TypeReference _newTypeReference = context.newTypeReference(Object.class);
                      boolean _notEquals = (!Objects.equal(_extendedClass, _newTypeReference));
                      if (_notEquals) {
                        _builder.append("int count=super.getPropertyCount();");
                        _builder.newLine();
                      } else {
                        _builder.append("int count=0;");
                        _builder.newLine();
                      }
                    }
                    _builder.append("return ");
                    Iterable<? extends MutableFieldDeclaration> _declaredFields_1 = clazz.getDeclaredFields();
                    int _size = IterableExtensions.size(_declaredFields_1);
                    _builder.append(_size, "");
                    _builder.append("+count;");
                    _builder.newLineIfNotEmpty();
                  }
                }
                return _builder;
              }
            };
            it.setBody(_function);
          }
        };
        clazz.addMethod(_simpleName_1, _function);
      } else {
        String _simpleName_2 = method.getSimpleName();
        boolean _equalsIgnoreCase_1 = _simpleName_2.equalsIgnoreCase("getProperty");
        if (_equalsIgnoreCase_1) {
          String _simpleName_3 = method.getSimpleName();
          final Procedure1<MutableMethodDeclaration> _function_1 = new Procedure1<MutableMethodDeclaration>() {
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
                  {
                    Iterable<? extends MutableFieldDeclaration> _declaredFields = clazz.getDeclaredFields();
                    boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(_declaredFields);
                    if (_isNullOrEmpty) {
                      _builder.append("return null;");
                      _builder.newLine();
                    } else {
                      {
                        TypeReference _extendedClass = clazz.getExtendedClass();
                        TypeReference _newTypeReference = context.newTypeReference(Object.class);
                        boolean _notEquals = (!Objects.equal(_extendedClass, _newTypeReference));
                        if (_notEquals) {
                          _builder.append("\t\t");
                          _builder.append("int count=super.getPropertyCount();");
                          _builder.newLine();
                        } else {
                          _builder.append("\t\t");
                          _builder.append("int count=0;");
                          _builder.newLine();
                        }
                      }
                      _builder.newLine();
                      {
                        Iterable<? extends MutableFieldDeclaration> _declaredFields_1 = clazz.getDeclaredFields();
                        int _size = IterableExtensions.size(_declaredFields_1);
                        int _minus = (_size - 1);
                        IntegerRange _upTo = new IntegerRange(0, _minus);
                        for(final Integer i : _upTo) {
                          _builder.append("\t\t");
                          _builder.append("if(index==count+");
                          _builder.append(i, "\t\t");
                          _builder.append("){");
                          _builder.newLineIfNotEmpty();
                          _builder.append("\t\t");
                          _builder.append("\t");
                          _builder.append("return ");
                          Iterable<? extends MutableFieldDeclaration> _declaredFields_2 = clazz.getDeclaredFields();
                          List<? extends MutableFieldDeclaration> _list = IterableExtensions.toList(_declaredFields_2);
                          MutableFieldDeclaration _get = _list.get((i).intValue());
                          String _simpleName = _get.getSimpleName();
                          _builder.append(_simpleName, "\t\t\t");
                          _builder.append(";");
                          _builder.newLineIfNotEmpty();
                          _builder.append("\t\t");
                          _builder.append("}\t");
                          _builder.newLine();
                        }
                      }
                      {
                        TypeReference _extendedClass_1 = clazz.getExtendedClass();
                        TypeReference _newTypeReference_1 = context.newTypeReference(Object.class);
                        boolean _notEquals_1 = (!Objects.equal(_extendedClass_1, _newTypeReference_1));
                        if (_notEquals_1) {
                          _builder.append("\t\t");
                          _builder.append("return super.getProperty(index);");
                          _builder.newLine();
                        } else {
                          _builder.append("\t\t");
                          _builder.append("return null;");
                          _builder.newLine();
                        }
                      }
                    }
                  }
                  return _builder;
                }
              };
              it.setBody(_function);
            }
          };
          clazz.addMethod(_simpleName_3, _function_1);
        } else {
          String _simpleName_4 = method.getSimpleName();
          boolean _equalsIgnoreCase_2 = _simpleName_4.equalsIgnoreCase("getPropertyInfo");
          if (_equalsIgnoreCase_2) {
            String _simpleName_5 = method.getSimpleName();
            final Procedure1<MutableMethodDeclaration> _function_2 = new Procedure1<MutableMethodDeclaration>() {
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
                    {
                      Iterable<? extends MutableFieldDeclaration> _declaredFields = clazz.getDeclaredFields();
                      boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(_declaredFields);
                      boolean _not = (!_isNullOrEmpty);
                      if (_not) {
                        {
                          TypeReference _extendedClass = clazz.getExtendedClass();
                          TypeReference _newTypeReference = context.newTypeReference(Object.class);
                          boolean _notEquals = (!Objects.equal(_extendedClass, _newTypeReference));
                          if (_notEquals) {
                            _builder.append("int count=super.getPropertyCount();");
                            _builder.newLine();
                          } else {
                            _builder.append("int count=0;");
                            _builder.newLine();
                          }
                        }
                        {
                          Iterable<? extends MutableFieldDeclaration> _declaredFields_1 = clazz.getDeclaredFields();
                          int _size = IterableExtensions.size(_declaredFields_1);
                          int _minus = (_size - 1);
                          IntegerRange _upTo = new IntegerRange(0, _minus);
                          for(final Integer i : _upTo) {
                            _builder.append("if(arg0==count+");
                            _builder.append(i, "");
                            _builder.append("){");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("arg2.type=");
                            Iterable<? extends MutableFieldDeclaration> _declaredFields_2 = clazz.getDeclaredFields();
                            List<? extends MutableFieldDeclaration> _list = IterableExtensions.toList(_declaredFields_2);
                            MutableFieldDeclaration _get = _list.get((i).intValue());
                            TypeReference _type = _get.getType();
                            TypeReference _wrapperIfPrimitive = _type.getWrapperIfPrimitive();
                            String _javaCode = it.toJavaCode(_wrapperIfPrimitive);
                            _builder.append(_javaCode, "\t");
                            _builder.append(".class;");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("arg2.name=\"");
                            Iterable<? extends MutableFieldDeclaration> _declaredFields_3 = clazz.getDeclaredFields();
                            List<? extends MutableFieldDeclaration> _list_1 = IterableExtensions.toList(_declaredFields_3);
                            MutableFieldDeclaration _get_1 = _list_1.get((i).intValue());
                            String _simpleName = _get_1.getSimpleName();
                            String _replace = _simpleName.replace("_", "");
                            _builder.append(_replace, "\t");
                            _builder.append("\";");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("}");
                            _builder.newLine();
                          }
                        }
                        {
                          TypeReference _extendedClass_1 = clazz.getExtendedClass();
                          TypeReference _newTypeReference_1 = context.newTypeReference(Object.class);
                          boolean _notEquals_1 = (!Objects.equal(_extendedClass_1, _newTypeReference_1));
                          if (_notEquals_1) {
                            _builder.append("super.getPropertyInfo(arg0,arg1,arg2);");
                            _builder.newLine();
                          }
                        }
                        _builder.newLine();
                      }
                    }
                    _builder.append("\t");
                    _builder.newLine();
                    return _builder;
                  }
                };
                it.setBody(_function);
              }
            };
            clazz.addMethod(_simpleName_5, _function_2);
          } else {
            String _simpleName_6 = method.getSimpleName();
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
                    {
                      Iterable<? extends MutableFieldDeclaration> _declaredFields = clazz.getDeclaredFields();
                      boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(_declaredFields);
                      boolean _not = (!_isNullOrEmpty);
                      if (_not) {
                        Iterable<? extends MutableFieldDeclaration> _declaredFields_1 = clazz.getDeclaredFields();
                        final Function1<MutableFieldDeclaration, Boolean> _function = new Function1<MutableFieldDeclaration, Boolean>() {
                          public Boolean apply(final MutableFieldDeclaration f) {
                            boolean _isFinal = f.isFinal();
                            return Boolean.valueOf((!_isFinal));
                          }
                        };
                        final Iterable<? extends MutableFieldDeclaration> fields = IterableExtensions.filter(_declaredFields_1, _function);
                        _builder.newLineIfNotEmpty();
                        {
                          TypeReference _extendedClass = clazz.getExtendedClass();
                          TypeReference _newTypeReference = context.newTypeReference(Object.class);
                          boolean _notEquals = (!Objects.equal(_extendedClass, _newTypeReference));
                          if (_notEquals) {
                            _builder.append("\t");
                            _builder.append("int count=super.getPropertyCount();");
                            _builder.newLine();
                          } else {
                            _builder.append("\t");
                            _builder.append("int count=0;");
                            _builder.newLine();
                          }
                        }
                        _builder.append("\t");
                        _builder.newLine();
                        {
                          int _size = IterableExtensions.size(fields);
                          int _minus = (_size - 1);
                          IntegerRange _upTo = new IntegerRange(0, _minus);
                          for(final Integer i : _upTo) {
                            _builder.append("\t");
                            _builder.append("if(arg0==count+");
                            _builder.append(i, "\t");
                            _builder.append("){");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t");
                            List<? extends MutableFieldDeclaration> _list = IterableExtensions.toList(fields);
                            MutableFieldDeclaration _get = _list.get((i).intValue());
                            String fieldName = _get.getSimpleName();
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t");
                            List<? extends MutableFieldDeclaration> _list_1 = IterableExtensions.toList(fields);
                            MutableFieldDeclaration _get_1 = _list_1.get((i).intValue());
                            TypeReference fieldType = _get_1.getType();
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("this.");
                            _builder.append(fieldName, "\t\t");
                            _builder.append("=");
                            TypeReference _wrapperIfPrimitive = fieldType.getWrapperIfPrimitive();
                            String _typeConverted = ksoapSerializableCompilationParticipant.this.typeConverted(_wrapperIfPrimitive, "arg1");
                            _builder.append(_typeConverted, "\t\t");
                            _builder.append(";");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("}");
                            _builder.newLine();
                          }
                        }
                        {
                          TypeReference _extendedClass_1 = clazz.getExtendedClass();
                          TypeReference _newTypeReference_1 = context.newTypeReference(Object.class);
                          boolean _notEquals_1 = (!Objects.equal(_extendedClass_1, _newTypeReference_1));
                          if (_notEquals_1) {
                            _builder.append("\t");
                            _builder.append("super.setProperty(arg0,arg1);");
                            _builder.newLine();
                          }
                        }
                      }
                    }
                    return _builder;
                  }
                };
                it.setBody(_function);
              }
            };
            clazz.addMethod(_simpleName_6, _function_3);
          }
        }
      }
    }
  }
  
  public MutableConstructorDeclaration addConstructor(final MutableClassDeclaration clazz, @Extension final TransformationContext context) {
    final Procedure1<MutableConstructorDeclaration> _function = new Procedure1<MutableConstructorDeclaration>() {
      public void apply(final MutableConstructorDeclaration it) {
        TypeReference _newTypeReference = context.newTypeReference(SoapObject.class);
        it.addParameter("object", _newTypeReference);
        Iterable<? extends MutableFieldDeclaration> _xifexpression = null;
        Iterable<? extends MutableFieldDeclaration> _declaredFields = clazz.getDeclaredFields();
        boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(_declaredFields);
        boolean _not = (!_isNullOrEmpty);
        if (_not) {
          Iterable<? extends MutableFieldDeclaration> _declaredFields_1 = clazz.getDeclaredFields();
          final Function1<MutableFieldDeclaration, Boolean> _function = new Function1<MutableFieldDeclaration, Boolean>() {
            public Boolean apply(final MutableFieldDeclaration f) {
              boolean _isFinal = f.isFinal();
              return Boolean.valueOf((!_isFinal));
            }
          };
          _xifexpression = IterableExtensions.filter(_declaredFields_1, _function);
        } else {
          _xifexpression = CollectionLiterals.emptyList();
        }
        final Iterable<? extends MutableFieldDeclaration> fields = _xifexpression;
        final CompilationStrategy _function_1 = new CompilationStrategy() {
          public CharSequence compile(final CompilationStrategy.CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            {
              TypeReference _extendedClass = clazz.getExtendedClass();
              TypeReference _newTypeReference = context.newTypeReference(Object.class);
              boolean _notEquals = (!Objects.equal(_extendedClass, _newTypeReference));
              if (_notEquals) {
                _builder.append("super(object);");
                _builder.newLine();
              }
            }
            {
              boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(fields);
              boolean _not = (!_isNullOrEmpty);
              if (_not) {
                {
                  int _size = IterableExtensions.size(fields);
                  int _minus = (_size - 1);
                  IntegerRange _upTo = new IntegerRange(0, _minus);
                  for(final Integer i : _upTo) {
                    final MutableFieldDeclaration a = ((MutableFieldDeclaration[])Conversions.unwrapArray(fields, MutableFieldDeclaration.class))[(i).intValue()];
                    _builder.newLineIfNotEmpty();
                    _builder.append("if(object.hasProperty(\"");
                    String _simpleName = a.getSimpleName();
                    String _replace = _simpleName.replace("_", "");
                    _builder.append(_replace, "");
                    _builder.append("\")){");
                    _builder.newLineIfNotEmpty();
                    TypeReference _newTypeReference_1 = context.newTypeReference(Object.class);
                    String _javaCode = it.toJavaCode(_newTypeReference_1);
                    _builder.append(_javaCode, "");
                    _builder.append(" obj=object.getProperty(\"");
                    String _simpleName_1 = a.getSimpleName();
                    String _replace_1 = _simpleName_1.replace("_", "");
                    _builder.append(_replace_1, "");
                    _builder.append("\");");
                    _builder.newLineIfNotEmpty();
                    _builder.newLine();
                    {
                      TypeReference _newTypeReference_2 = context.newTypeReference(KsoapObject.class);
                      Type _type = _newTypeReference_2.getType();
                      AnnotationReference _findAnnotation = a.findAnnotation(_type);
                      boolean _equals = Objects.equal(_findAnnotation, null);
                      if (_equals) {
                        _builder.append("if(obj!=null && obj.getClass().equals(SoapPrimitive.class)){");
                        _builder.newLine();
                        _builder.append("\t");
                        TypeReference _newTypeReference_3 = context.newTypeReference(SoapPrimitive.class);
                        String _javaCode_1 = it.toJavaCode(_newTypeReference_3);
                        _builder.append(_javaCode_1, "\t");
                        _builder.append(" value=(SoapPrimitive) obj;");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("if(value.toString()!=null){");
                        _builder.newLine();
                        _builder.newLine();
                        _builder.append("\t\t");
                        _builder.append("this.");
                        String _simpleName_2 = a.getSimpleName();
                        _builder.append(_simpleName_2, "\t\t");
                        _builder.append("=");
                        TypeReference _type_1 = a.getType();
                        TypeReference _wrapperIfPrimitive = _type_1.getWrapperIfPrimitive();
                        String _typeConverted = ksoapSerializableCompilationParticipant.this.typeConverted(_wrapperIfPrimitive, "value");
                        _builder.append(_typeConverted, "\t\t");
                        _builder.append(";");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("}");
                        _builder.newLine();
                        _builder.append("}else if(obj!=null && obj instanceof ");
                        TypeReference _type_2 = a.getType();
                        TypeReference _wrapperIfPrimitive_1 = _type_2.getWrapperIfPrimitive();
                        String _simpleName_3 = _wrapperIfPrimitive_1.getSimpleName();
                        _builder.append(_simpleName_3, "");
                        _builder.append("){");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("this.");
                        String _simpleName_4 = a.getSimpleName();
                        _builder.append(_simpleName_4, "\t");
                        _builder.append("=(");
                        TypeReference _type_3 = a.getType();
                        TypeReference _wrapperIfPrimitive_2 = _type_3.getWrapperIfPrimitive();
                        _builder.append(_wrapperIfPrimitive_2, "\t");
                        _builder.append(" ) obj;");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("}");
                        _builder.newLine();
                      } else {
                        _builder.append("this.");
                        String _simpleName_5 = a.getSimpleName();
                        _builder.append(_simpleName_5, "");
                        _builder.append("=new ");
                        TypeReference _type_4 = a.getType();
                        String _simpleName_6 = _type_4.getSimpleName();
                        _builder.append(_simpleName_6, "");
                        _builder.append("((SoapObject) obj);");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                    _builder.append("}");
                    _builder.newLine();
                  }
                }
              }
            }
            _builder.newLine();
            return _builder;
          }
        };
        it.setBody(_function_1);
      }
    };
    return clazz.addConstructor(_function);
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
}
