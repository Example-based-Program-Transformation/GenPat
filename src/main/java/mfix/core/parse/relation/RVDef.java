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
 * @date: 2018/12/5
 */
public class RVDef extends RDef {

    /**
     * value of a constant
     */
    private Object _value;

    public RVDef() {
        super(RelationKind.VIRTUALDEFINE);
    }

    public void setValue(Object value) {
        _value = value;
    }

    public Object getValue() {
        return _value;
    }

    @Override
    public void doAbstraction(double frequency) {

    }

    @Override
    public String getExprString() {
        if(_name != null) {
            return _name;
        }
        if(_value != null) {
            return _value.toString();
        }
        return null;
    }

    @Override
    public boolean match(Relation relation, Set<Pair<Relation, Relation>> dependencies) {
        if (!super.match(relation, dependencies)) {
            return false;
        }
        RVDef def = (RVDef) relation;
        if(_value == null) {
            return def.getValue() == null;
        }
        return _value.equals(def.getValue());
    }

    @Override
    public String toString() {
        return getExprString();
    }
}