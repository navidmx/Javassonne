package com.navidmx.javassonne.core;

/**
 * A representation of features that can include multiple segments (e.g. roads, cities, technically fields).
 */
interface CompoundFeature extends Feature {
    /**
     * Merges the feature with the provided one.
     * @param f Feature to be merged.
     */
    void merge(CompoundFeature f);
}
