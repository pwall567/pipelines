package net.pwall.pipeline;

public class ForkPipeline<A, R> extends AbstractPipeline<A, A, R> {

    private final Acceptor<? super A, ? extends R> downstream2;

    /**
     * Construct a {@code Fork} with the given downstream {@link Acceptor}.
     *
     * @param downstream1 the {@link Acceptor}
     * @param downstream2 the {@link Acceptor}
     */
    public ForkPipeline(Acceptor<? super A, ? extends R> downstream1, Acceptor<? super A, ? extends R> downstream2) {
        super(downstream1);
        this.downstream2 = downstream2;
    }

    @Override
    public void acceptObject(A value) throws Exception {
        emit(value);
        downstream2.accept(value);
    }

}
