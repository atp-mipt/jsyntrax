package org.atpfivt.jsyntrax.visitors;

import org.atpfivt.jsyntrax.generators.SVGCanvasBuilder.UnitEndPoint;
import org.atpfivt.jsyntrax.units.nodes.Bullet;
import org.atpfivt.jsyntrax.units.nodes.Node;
import org.atpfivt.jsyntrax.units.nodes.NoneNode;
import org.atpfivt.jsyntrax.units.tracks.Choice;
import org.atpfivt.jsyntrax.units.tracks.Line;
import org.atpfivt.jsyntrax.units.tracks.loop.Loop;
import org.atpfivt.jsyntrax.units.tracks.loop.Toploop;
import org.atpfivt.jsyntrax.units.tracks.opt.Opt;
import org.atpfivt.jsyntrax.units.tracks.opt.Optx;
import org.atpfivt.jsyntrax.units.tracks.stack.Indentstack;
import org.atpfivt.jsyntrax.units.tracks.stack.Rightstack;
import org.atpfivt.jsyntrax.units.tracks.stack.Stack;

public class CanvasBuilderVisitor extends TemplateVisitor<UnitEndPoint> {
  @Override
  public void visit(Line unit) {

  }

  @Override
  public void visit(Loop unit) {

  }

  @Override
  public void visit(Toploop unit) {

  }

  @Override
  public void visit(Choice unit) {

  }

  @Override
  public void visit(Opt unit) {

  }

  @Override
  public void visit(Optx unit) {

  }

  @Override
  public void visit(Stack unit) {

  }

  @Override
  public void visit(Rightstack unit) {

  }

  @Override
  public void visit(Indentstack unit) {

  }

  @Override
  public void visit(Bullet unit) {

  }

  @Override
  public void visit(Node unit) {

  }

  @Override
  public void visit(NoneNode unit) {

  }
}