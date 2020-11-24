package org.atpfivt.jsyntrax.visitors;

public abstract class TemplateVisitor<T> implements Visitor {
  T tos_value_;

  public T getTosValue() {
    return tos_value_;
  }

  public void setTosValue(T tos_value) {
    tos_value_ = tos_value;
  }
}
