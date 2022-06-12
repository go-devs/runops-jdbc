package ninja.ebanx.runops.utils;


import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;

/**
 * The methods in this class allow to cast nullable reference to a non-nullable one.
 * This is an internal class, and it is not meant to be used as a public API.
 */
@SuppressWarnings({"cast.unsafe", "NullableProblems", "contracts.postcondition.not.satisfied"})
public class Nullness {
    @Pure
    public static @EnsuresNonNull("#1") <T extends @Nullable Object> @NonNull T castNonNull(
            @Nullable T ref) {
        assert ref != null : "Misuse of castNonNull: called with a null argument";
        return (@NonNull T) ref;
    }

    @Pure
    public static @EnsuresNonNull("#1") <T extends @Nullable Object> @NonNull T castNonNull(
            @Nullable T ref, String message) {
        assert ref != null : "Misuse of castNonNull: called with a null argument " + message;
        return (@NonNull T) ref;
    }
}