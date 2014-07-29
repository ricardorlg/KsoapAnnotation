package serializables;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.eclipse.xtend.lib.macro.Active;
import serializables.ksoapListSerializableCompilationParticipant;

@Active(ksoapListSerializableCompilationParticipant.class)
@Target(ElementType.TYPE)
public @interface KsoapListSerializable {
  public String nombre();
}
