/**
 * Copyright (C) SEI, PKU, PRC. - All Rights Reserved.
 * Unauthorized copying of this file via any medium is
 * strictly prohibited Proprietary and Confidential.
 * Written by Jiajun Jiang<jiajun.jiang@pku.edu.cn>.
 */

package mfix.core.parse.relation;

import mfix.common.util.Pair;

import java.util.Set;

/**
 * @author: Jiajun
 * @date: 2018/11/29
 */
public class RAssign extends ObjRelation {
    /**
     * the following assignment operators should be
     * normalized to an {@code ROpt} relation
     * and an {@code RAssign} relation
     *
     * =, +=, -=, *=. /=, %=, <<=, >>=
     * &=, |=, ^=
     *
     */

    /**
     * Left hand side of the assignment
     */
    private ObjRelation _lhs;
    /**
     * Rgiht hand side of the assignment
     */
    private ObjRelation _rhs;

    public RAssign(ObjRelation lhs) {
        super(RelationKind.ASSIGN);
        _lhs = lhs;
        _lhs.usedBy(this);
    }

    public void setLhs(ObjRelation lhs) {
        _lhs = lhs;
        _lhs.usedBy(this);
    }

    public void setRhs(ObjRelation rhs) {
        _rhs = rhs;
        _rhs.usedBy(this);
    }

    public ObjRelation getLhs() {
        return _lhs;
    }

    public ObjRelation getRhs() {
        return _rhs;
    }

    @Override
    protected Set<Relation> expandDownward0(Set<Relation> set) {
        set.add(_lhs);
        set.add(_rhs);
        return set;
    }

    @Override
    public String getExprString() {
        return _lhs.getExprString();
    }

    @Override
    public void doAbstraction(double frequency) {

    }

    @Override
    public boolean match(Relation relation, Set<Pair<Relation, Relation>> denpendencies) {
        if(!super.match(relation, denpendencies)) {
            return false;
        }
        RAssign assign = (RAssign) relation;
        if (_lhs.match(assign.getLhs(), denpendencies) && _rhs.match(assign.getRhs(), denpendencies)) {
            denpendencies.add(new Pair<>(_lhs, assign.getLhs()));
            denpendencies.add(new Pair<>(_rhs, assign.getRhs()));
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return _lhs.getExprString() + "=" + _rhs.getExprString();
    }
}