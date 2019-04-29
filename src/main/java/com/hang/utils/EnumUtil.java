package com.hang.utils;

/**
 * @author test
 */
public class EnumUtil {

  public static boolean isDefined(Enum[] enums, String value) {
    for (Enum e : enums) {
      if (e.name().equals(value)) {
        return true;
      }
    }
    return false;
  }

}
