package com.jongsoft.lang.perfmon;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Timed {

    /**
     * The name of the entry begin timed by this annotation.
     *
     * @return  the actual name
     */
    String name();

    /**
     * <p>
     *  The process to which this timed entry belongs to. This can be used to group several timed
     *  entries by a logical process.
     * </p>
     *
     * <b>Default: </b> "None"
     *
     * @return  the process name
     */
    String process() default "None";

}
