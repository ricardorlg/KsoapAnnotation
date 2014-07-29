package services;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.AnnotationTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.Type;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtend.lib.macro.declaration.Visibility;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import services.KsoapService;

@SuppressWarnings("all")
public class KsoapServiceCompilationParticipant extends AbstractClassProcessor {
  private List<String> validTypes = Collections.<String>unmodifiableList(Lists.<String>newArrayList("Boolean", "Long", "Integer", "String", "Float", "Double", "Date", "byte[]", "Character"));
  
  public void doTransform(final List<? extends MutableClassDeclaration> annotatedClasses, @Extension final TransformationContext context) {
    final Procedure1<MutableClassDeclaration> _function = new Procedure1<MutableClassDeclaration>() {
      public void apply(final MutableClassDeclaration it) {
        KsoapServiceCompilationParticipant.this.doTransform(it, context);
      }
    };
    IterableExtensions.forEach(annotatedClasses, _function);
  }
  
  public void doTransform(final MutableClassDeclaration clazz, @Extension final TransformationContext context) {
    this.createFields(clazz, context);
    this.createServiceConsumeMethod(clazz, context);
    this.createServiceMethod(clazz, context);
  }
  
  public MutableMethodDeclaration createServiceConsumeMethod(final MutableClassDeclaration clazz, @Extension final TransformationContext context) {
    final Procedure1<MutableMethodDeclaration> _function = new Procedure1<MutableMethodDeclaration>() {
      public void apply(final MutableMethodDeclaration it) {
        it.setVisibility(Visibility.PUBLIC);
        TypeReference _newTypeReference = context.newTypeReference(SoapObject.class);
        it.setReturnType(_newTypeReference);
        TypeReference _newTypeReference_1 = context.newTypeReference(Exception.class);
        it.setExceptions(new TypeReference[] { _newTypeReference_1 });
        final TypeReference[] clases = KsoapServiceCompilationParticipant.this.getArrayClassValue(clazz, context, "inputsParametersTypes");
        final String[] nombres = KsoapServiceCompilationParticipant.this.getArrayStringValue(clazz, context, "inputsParametersNames");
        int _size = ((List<TypeReference>)Conversions.doWrapArray(clases)).size();
        int _size_1 = ((List<String>)Conversions.doWrapArray(nombres)).size();
        boolean _notEquals = (_size != _size_1);
        if (_notEquals) {
          Iterable<? extends AnnotationReference> _annotations = clazz.getAnnotations();
          AnnotationReference _head = IterableExtensions.head(_annotations);
          context.addError(_head, 
            "Error parametros no iguales nombres y tipos deben tener la misma dimension");
        }
        int _size_2 = ((List<TypeReference>)Conversions.doWrapArray(clases)).size();
        int _minus = (_size_2 - 1);
        IntegerRange _upTo = new IntegerRange(0, _minus);
        for (final Integer i : _upTo) {
          String _get = nombres[(i).intValue()];
          TypeReference _get_1 = clases[(i).intValue()];
          it.addParameter(_get, _get_1);
        }
        final CompilationStrategy _function = new CompilationStrategy() {
          public CharSequence compile(final CompilationStrategy.CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            TypeReference _newTypeReference = context.newTypeReference(SoapObject.class);
            String _javaCode = it.toJavaCode(_newTypeReference);
            _builder.append(_javaCode, "");
            _builder.append(" request = new SoapObject(NAME_SPACE,METHOD_NAME);");
            _builder.newLineIfNotEmpty();
            {
              int _size = ((List<TypeReference>)Conversions.doWrapArray(clases)).size();
              int _minus = (_size - 1);
              IntegerRange _upTo = new IntegerRange(0, _minus);
              for(final Integer i : _upTo) {
                TypeReference _newTypeReference_1 = context.newTypeReference(PropertyInfo.class);
                String _javaCode_1 = it.toJavaCode(_newTypeReference_1);
                _builder.append(_javaCode_1, "");
                _builder.append(" propertyInfo");
                _builder.append(i, "");
                _builder.append(" = new PropertyInfo();");
                _builder.newLineIfNotEmpty();
                _builder.append("propertyInfo");
                _builder.append(i, "");
                _builder.append(".setName(\"");
                String _get = nombres[(i).intValue()];
                _builder.append(_get, "");
                _builder.append("\");");
                _builder.newLineIfNotEmpty();
                _builder.append("propertyInfo");
                _builder.append(i, "");
                _builder.append(".setValue(");
                String _get_1 = nombres[(i).intValue()];
                _builder.append(_get_1, "");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.append("propertyInfo");
                _builder.append(i, "");
                _builder.append(".setType(");
                TypeReference _get_2 = clases[(i).intValue()];
                String _simpleName = _get_2.getSimpleName();
                _builder.append(_simpleName, "");
                _builder.append(".class);");
                _builder.newLineIfNotEmpty();
                _builder.append("request.addProperty(propertyInfo");
                _builder.append(i, "");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
            TypeReference _newTypeReference_2 = context.newTypeReference(SoapSerializationEnvelope.class);
            String _javaCode_2 = it.toJavaCode(_newTypeReference_2);
            _builder.append(_javaCode_2, "");
            _builder.append(" envelope = new SoapSerializationEnvelope(");
            TypeReference _newTypeReference_3 = context.newTypeReference(SoapEnvelope.class);
            String _javaCode_3 = it.toJavaCode(_newTypeReference_3);
            _builder.append(_javaCode_3, "");
            _builder.append(".VER11);");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("envelope.setOutputSoapObject(request);");
            _builder.newLine();
            _builder.append("new ");
            TypeReference _newTypeReference_4 = context.newTypeReference(MarshalDate.class);
            String _javaCode_4 = it.toJavaCode(_newTypeReference_4);
            _builder.append(_javaCode_4, "");
            _builder.append("().register(envelope);");
            _builder.newLineIfNotEmpty();
            _builder.append("new ");
            TypeReference _newTypeReference_5 = context.newTypeReference(MarshalBase64.class);
            String _javaCode_5 = it.toJavaCode(_newTypeReference_5);
            _builder.append(_javaCode_5, "");
            _builder.append("().register(envelope);");
            _builder.newLineIfNotEmpty();
            _builder.append("try {");
            _builder.newLine();
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            TypeReference _newTypeReference_6 = context.newTypeReference(HttpTransportSE.class);
            String _javaCode_6 = it.toJavaCode(_newTypeReference_6);
            _builder.append(_javaCode_6, "\t");
            _builder.append(" transp = new HttpTransportSE(URL, 6000);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("transp.debug = true;");
            _builder.newLine();
            _builder.append("\t");
            TypeReference _newTypeReference_7 = context.newTypeReference(System.class);
            String _javaCode_7 = it.toJavaCode(_newTypeReference_7);
            _builder.append(_javaCode_7, "\t");
            _builder.append(".setProperty(\"http.keepAlive\", \"false\");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("transp.call(NAME_SPACE + METHOD_NAME, envelope);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("android.util.Log.i(\"REQUEST--->\", transp.requestDump);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("android.util.Log.i(\"RESPONSE--->\", transp.responseDump);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Object result = envelope.bodyIn;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("SoapObject _retObject = (SoapObject) result;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("if (result instanceof ");
            TypeReference _newTypeReference_8 = context.newTypeReference(SoapFault.class);
            String _javaCode_8 = it.toJavaCode(_newTypeReference_8);
            _builder.append(_javaCode_8, "\t");
            _builder.append(") {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("SoapFault fault = (SoapFault) result;");
            _builder.newLine();
            _builder.append("\t\t");
            _builder.append("throw new Exception(fault.toString());");
            _builder.newLine();
            _builder.append("\t\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("transp.reset();");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("return _retObject;");
            _builder.newLine();
            _builder.append("} catch (");
            TypeReference _newTypeReference_9 = context.newTypeReference(HttpResponseException.class);
            String _javaCode_9 = it.toJavaCode(_newTypeReference_9);
            _builder.append(_javaCode_9, "");
            _builder.append(" ex2) {");
            _builder.newLineIfNotEmpty();
            _builder.append("String message;");
            _builder.newLine();
            _builder.append("if(ex2.getLocalizedMessage()!=null){");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("message= ex2.getLocalizedMessage()+\" Code =\"+ex2.getStatusCode();");
            _builder.newLine();
            _builder.append("}else{");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("message=\"Error HttpCode = \"+ex2.getStatusCode();");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            _builder.append("android.util.Log.d(\"httpResponseError\", message);");
            _builder.newLine();
            _builder.append("\t\t");
            _builder.append("throw ex2;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("} catch (");
            TypeReference _newTypeReference_10 = context.newTypeReference(IOException.class);
            String _javaCode_10 = it.toJavaCode(_newTypeReference_10);
            _builder.append(_javaCode_10, "\t");
            _builder.append(" ex) {");
            _builder.newLineIfNotEmpty();
            _builder.append("String message;");
            _builder.newLine();
            _builder.append("if(ex.getLocalizedMessage()!=null){");
            _builder.newLine();
            _builder.append("message= ex.getLocalizedMessage();");
            _builder.newLine();
            _builder.append("}else{");
            _builder.newLine();
            _builder.append("message=\"Error desconocido\";");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            _builder.append("android.util.Log.d(\"IOError\", message);");
            _builder.newLine();
            _builder.append("\t\t");
            _builder.append("throw ex;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            return _builder;
          }
        };
        it.setBody(_function);
      }
    };
    return clazz.addMethod("Execute", _function);
  }
  
  public MutableMethodDeclaration createServiceMethod(final MutableClassDeclaration clazz, @Extension final TransformationContext context) {
    MutableMethodDeclaration _xblockexpression = null;
    {
      final String method_name = this.getStringValue(clazz, context, "METHOD_NAME");
      final TypeReference returnedType = this.getClassValue(clazz, context, "typeReturn");
      String _lowerCase = method_name.toLowerCase();
      String _firstUpper = StringExtensions.toFirstUpper(_lowerCase);
      String _plus = ("do" + _firstUpper);
      final Procedure1<MutableMethodDeclaration> _function = new Procedure1<MutableMethodDeclaration>() {
        public void apply(final MutableMethodDeclaration it) {
          it.setVisibility(Visibility.PUBLIC);
          it.setReturnType(returnedType);
          TypeReference _newTypeReference = context.newTypeReference(Exception.class);
          it.setExceptions(new TypeReference[] { _newTypeReference });
          final TypeReference[] clases = KsoapServiceCompilationParticipant.this.getArrayClassValue(clazz, context, "inputsParametersTypes");
          final String[] nombres = KsoapServiceCompilationParticipant.this.getArrayStringValue(clazz, context, "inputsParametersNames");
          final Boolean returnTypeimplicit = KsoapServiceCompilationParticipant.this.getBooleanValue(clazz, context, "implicitReturn");
          int _size = ((List<TypeReference>)Conversions.doWrapArray(clases)).size();
          int _size_1 = ((List<String>)Conversions.doWrapArray(nombres)).size();
          boolean _notEquals = (_size != _size_1);
          if (_notEquals) {
            Iterable<? extends AnnotationReference> _annotations = clazz.getAnnotations();
            AnnotationReference _head = IterableExtensions.head(_annotations);
            context.addError(_head, 
              "Error parametros no iguales nombres y tipos deben tener la misma dimension");
          }
          int _size_2 = ((List<TypeReference>)Conversions.doWrapArray(clases)).size();
          int _minus = (_size_2 - 1);
          IntegerRange _upTo = new IntegerRange(0, _minus);
          for (final Integer i : _upTo) {
            String _get = nombres[(i).intValue()];
            TypeReference _get_1 = clases[(i).intValue()];
            it.addParameter(_get, _get_1);
          }
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationStrategy.CompilationContext it) {
              StringConcatenation _builder = new StringConcatenation();
              _builder.newLine();
              {
                String _simpleName = returnedType.getSimpleName();
                boolean _contains = KsoapServiceCompilationParticipant.this.validTypes.contains(_simpleName);
                if (_contains) {
                  _builder.append("SoapObject rpta=Execute(");
                  String _string = ((List<String>)Conversions.doWrapArray(nombres)).toString();
                  String _replace = _string.replace("[", "");
                  String _replace_1 = _replace.replace("]", "");
                  _builder.append(_replace_1, "");
                  _builder.append(");");
                  _builder.newLineIfNotEmpty();
                  _builder.append("Object obj = rpta.getProperty(\"return\");");
                  _builder.newLine();
                  _builder.append("if (obj != null && obj.getClass().equals(SoapPrimitive.class))");
                  _builder.newLine();
                  _builder.append("{");
                  _builder.newLine();
                  TypeReference _newTypeReference = context.newTypeReference(SoapPrimitive.class);
                  String _javaCode = it.toJavaCode(_newTypeReference);
                  _builder.append(_javaCode, "");
                  _builder.append(" j =(SoapPrimitive) rpta.getProperty(\"return\");");
                  _builder.newLineIfNotEmpty();
                  _builder.append("return ");
                  String _typeConverted = KsoapServiceCompilationParticipant.this.typeConverted(returnedType, "j");
                  _builder.append(_typeConverted, "");
                  _builder.append(";");
                  _builder.newLineIfNotEmpty();
                  _builder.append("}");
                  _builder.newLine();
                  _builder.append("return null;");
                  _builder.newLine();
                  _builder.newLine();
                } else {
                  _builder.append("SoapObject rpta=Execute(");
                  String _string_1 = ((List<String>)Conversions.doWrapArray(nombres)).toString();
                  String _replace_2 = _string_1.replace("[", "");
                  String _replace_3 = _replace_2.replace("]", "");
                  _builder.append(_replace_3, "");
                  _builder.append(");");
                  _builder.newLineIfNotEmpty();
                  {
                    if ((returnTypeimplicit).booleanValue()) {
                      _builder.append("Object obj = rpta.getProperty(\"return\");");
                      _builder.newLine();
                      _builder.append("if (obj != null && obj.getClass().equals(SoapObject.class))");
                      _builder.newLine();
                      _builder.append("{");
                      _builder.newLine();
                      TypeReference _newTypeReference_1 = context.newTypeReference(SoapObject.class);
                      String _javaCode_1 = it.toJavaCode(_newTypeReference_1);
                      _builder.append(_javaCode_1, "");
                      _builder.append(" j =(SoapObject) rpta.getProperty(\"return\");");
                      _builder.newLineIfNotEmpty();
                      _builder.append("return new ");
                      String _javaCode_2 = it.toJavaCode(returnedType);
                      _builder.append(_javaCode_2, "");
                      _builder.append(" (j);");
                      _builder.newLineIfNotEmpty();
                      _builder.append("}");
                      _builder.newLine();
                      _builder.append("return null;");
                      _builder.newLine();
                    } else {
                      _builder.append("return new ");
                      String _javaCode_3 = it.toJavaCode(returnedType);
                      _builder.append(_javaCode_3, "");
                      _builder.append(" (rpta);");
                      _builder.newLineIfNotEmpty();
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
      _xblockexpression = clazz.addMethod(_plus, _function);
    }
    return _xblockexpression;
  }
  
  public MutableFieldDeclaration createFields(final MutableClassDeclaration clazz, @Extension final TransformationContext context) {
    MutableFieldDeclaration _xblockexpression = null;
    {
      final Procedure1<MutableFieldDeclaration> _function = new Procedure1<MutableFieldDeclaration>() {
        public void apply(final MutableFieldDeclaration it) {
          it.setFinal(true);
          TypeReference _newTypeReference = context.newTypeReference(String.class);
          it.setType(_newTypeReference);
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationStrategy.CompilationContext it) {
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("\"");
              String _stringValue = KsoapServiceCompilationParticipant.this.getStringValue(clazz, context, "URL");
              _builder.append(_stringValue, "");
              _builder.append("\"");
              return _builder;
            }
          };
          it.setInitializer(_function);
        }
      };
      clazz.addField("URL", _function);
      final Procedure1<MutableFieldDeclaration> _function_1 = new Procedure1<MutableFieldDeclaration>() {
        public void apply(final MutableFieldDeclaration it) {
          TypeReference _newTypeReference = context.newTypeReference(String.class);
          it.setType(_newTypeReference);
          it.setFinal(true);
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationStrategy.CompilationContext it) {
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("\"");
              String _stringValue = KsoapServiceCompilationParticipant.this.getStringValue(clazz, context, "NAME_SPACE");
              _builder.append(_stringValue, "");
              _builder.append("\"");
              return _builder;
            }
          };
          it.setInitializer(_function);
        }
      };
      clazz.addField("NAME_SPACE", _function_1);
      final Procedure1<MutableFieldDeclaration> _function_2 = new Procedure1<MutableFieldDeclaration>() {
        public void apply(final MutableFieldDeclaration it) {
          it.setFinal(true);
          TypeReference _newTypeReference = context.newTypeReference(String.class);
          it.setType(_newTypeReference);
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationStrategy.CompilationContext it) {
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("\"");
              String _stringValue = KsoapServiceCompilationParticipant.this.getStringValue(clazz, context, "METHOD_NAME");
              _builder.append(_stringValue, "");
              _builder.append("\"");
              return _builder;
            }
          };
          it.setInitializer(_function);
        }
      };
      _xblockexpression = clazz.addField("METHOD_NAME", _function_2);
    }
    return _xblockexpression;
  }
  
  public String getStringValue(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context, final String propertyName) {
    Iterable<? extends AnnotationReference> _annotations = annotatedClass.getAnnotations();
    final Function1<AnnotationReference, Boolean> _function = new Function1<AnnotationReference, Boolean>() {
      public Boolean apply(final AnnotationReference it) {
        AnnotationTypeDeclaration _annotationTypeDeclaration = it.getAnnotationTypeDeclaration();
        TypeReference _newTypeReference = context.newTypeReference(KsoapService.class);
        Type _type = _newTypeReference.getType();
        return Boolean.valueOf(Objects.equal(_annotationTypeDeclaration, _type));
      }
    };
    AnnotationReference _findFirst = IterableExtensions.findFirst(_annotations, _function);
    final Object value = _findFirst.getValue(propertyName);
    boolean _equals = Objects.equal(value, null);
    if (_equals) {
      return null;
    }
    return value.toString();
  }
  
  public Boolean getBooleanValue(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context, final String propertyName) {
    Iterable<? extends AnnotationReference> _annotations = annotatedClass.getAnnotations();
    final Function1<AnnotationReference, Boolean> _function = new Function1<AnnotationReference, Boolean>() {
      public Boolean apply(final AnnotationReference it) {
        AnnotationTypeDeclaration _annotationTypeDeclaration = it.getAnnotationTypeDeclaration();
        TypeReference _newTypeReference = context.newTypeReference(KsoapService.class);
        Type _type = _newTypeReference.getType();
        return Boolean.valueOf(Objects.equal(_annotationTypeDeclaration, _type));
      }
    };
    AnnotationReference _findFirst = IterableExtensions.findFirst(_annotations, _function);
    final Object value = _findFirst.getValue(propertyName);
    boolean _equals = Objects.equal(value, null);
    if (_equals) {
      return null;
    }
    return ((Boolean) value);
  }
  
  public String[] getArrayStringValue(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context, final String propertyName) {
    Iterable<? extends AnnotationReference> _annotations = annotatedClass.getAnnotations();
    final Function1<AnnotationReference, Boolean> _function = new Function1<AnnotationReference, Boolean>() {
      public Boolean apply(final AnnotationReference it) {
        AnnotationTypeDeclaration _annotationTypeDeclaration = it.getAnnotationTypeDeclaration();
        TypeReference _newTypeReference = context.newTypeReference(KsoapService.class);
        Type _type = _newTypeReference.getType();
        return Boolean.valueOf(Objects.equal(_annotationTypeDeclaration, _type));
      }
    };
    AnnotationReference _findFirst = IterableExtensions.findFirst(_annotations, _function);
    final Object value = _findFirst.getValue(propertyName);
    boolean _equals = Objects.equal(value, null);
    if (_equals) {
      return null;
    }
    return ((String[]) value);
  }
  
  public TypeReference[] getArrayClassValue(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context, final String propertyName) {
    Iterable<? extends AnnotationReference> _annotations = annotatedClass.getAnnotations();
    final Function1<AnnotationReference, Boolean> _function = new Function1<AnnotationReference, Boolean>() {
      public Boolean apply(final AnnotationReference it) {
        AnnotationTypeDeclaration _annotationTypeDeclaration = it.getAnnotationTypeDeclaration();
        TypeReference _newTypeReference = context.newTypeReference(KsoapService.class);
        Type _type = _newTypeReference.getType();
        return Boolean.valueOf(Objects.equal(_annotationTypeDeclaration, _type));
      }
    };
    AnnotationReference _findFirst = IterableExtensions.findFirst(_annotations, _function);
    final TypeReference[] value = _findFirst.getClassArrayValue(propertyName);
    boolean _equals = Objects.equal(value, null);
    if (_equals) {
      return null;
    }
    return value;
  }
  
  public TypeReference getClassValue(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context, final String propertyName) {
    Iterable<? extends AnnotationReference> _annotations = annotatedClass.getAnnotations();
    final Function1<AnnotationReference, Boolean> _function = new Function1<AnnotationReference, Boolean>() {
      public Boolean apply(final AnnotationReference it) {
        AnnotationTypeDeclaration _annotationTypeDeclaration = it.getAnnotationTypeDeclaration();
        TypeReference _newTypeReference = context.newTypeReference(KsoapService.class);
        Type _type = _newTypeReference.getType();
        return Boolean.valueOf(Objects.equal(_annotationTypeDeclaration, _type));
      }
    };
    AnnotationReference _findFirst = IterableExtensions.findFirst(_annotations, _function);
    final Object value = _findFirst.getValue(propertyName);
    boolean _equals = Objects.equal(value, null);
    if (_equals) {
      return null;
    }
    return ((TypeReference) value);
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
