package com.syntrax.visitors;

import com.syntrax.generators.SVGCanvasBuilder.UnitEndPoint;
import com.syntrax.units.nodes.Bullet;
import com.syntrax.units.nodes.Node;
import com.syntrax.units.nodes.NoneNode;
import com.syntrax.units.tracks.Choice;
import com.syntrax.units.tracks.Line;
import com.syntrax.units.tracks.loop.Loop;
import com.syntrax.units.tracks.loop.Toploop;
import com.syntrax.units.tracks.opt.Opt;
import com.syntrax.units.tracks.opt.Optx;
import com.syntrax.units.tracks.stack.Indentstack;
import com.syntrax.units.tracks.stack.Rightstack;
import com.syntrax.units.tracks.stack.Stack;

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