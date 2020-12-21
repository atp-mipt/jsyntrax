package org.atpfivt.jsyntrax.visitors;

import org.atpfivt.jsyntrax.Configuration;
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

public interface Visitor {
  void visitLine(Line unit);
  void visitLoop(Loop unit);
  void visitToploop(Toploop unit);
  void visitChoice(Choice unit);
  void visitOpt(Opt unit);
  void visitOptx(Optx unit);
  void visitStack(Stack unit);
  void visitRightstack(Rightstack unit);
  void visitIndentstack(Indentstack unit);
  void visitBullet(Bullet unit);
  void visitNode(Node unit);
  void visitNoneNode(NoneNode unit);
  void visitConfiguration(Configuration unit);
}
