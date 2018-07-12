package com.ckw.rxjavalearning.process;

/**
 * Created by ckw
 * on 2018/7/12.
 */
public final class TopCreate extends Top{
    final TopOnSubscribe topOnSubscribe;

    public TopCreate(TopOnSubscribe topOnSubscribe) {
        this.topOnSubscribe = topOnSubscribe;
    }


    @Override
    protected void subscribeActual(Bottom bottom) {
        CreateCenter createCenter = new CreateCenter(bottom);
        topOnSubscribe.subscribe(createCenter);
    }

    static final class CreateCenter implements Center{

        final Bottom bottom;

        public CreateCenter(Bottom bottom) {
            this.bottom = bottom;
        }

        @Override
        public void onNext(String value) {
            bottom.onNext(value);
        }
    }
}
