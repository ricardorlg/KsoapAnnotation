package services;

import org.eclipse.xtend.lib.macro.Active;
import services.KsoapServiceCompilationParticipant;

@Active(KsoapServiceCompilationParticipant.class)
public @interface KsoapService {
  public String URL();
  public String NAME_SPACE();
  public String METHOD_NAME();
  public String[] inputsParametersNames();
  public Class<?>[] inputsParametersTypes();
  public Class<?> typeReturn();
  public boolean implicitReturn() default true;
  public boolean implicitTypes() default false;
}
