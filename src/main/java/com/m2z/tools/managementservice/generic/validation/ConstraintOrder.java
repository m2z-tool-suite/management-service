package com.m2z.tools.managementservice.generic.validation;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

public abstract class ConstraintOrder {
    @GroupSequence({
            First.class,
            Default.class,
            Second.class,
            Third.class,
            Fourth.class,
            Fifth.class,
            Sixth.class,
            Seventh.class,
            Eight.class})
    public interface ValidationSequence { }
    public interface First { }
    public interface Second { }

    public interface Third { }

    public interface Fourth { }

    public interface Fifth { }

    public interface Sixth { }

    public interface Seventh { }
    public interface Eight { }
}
