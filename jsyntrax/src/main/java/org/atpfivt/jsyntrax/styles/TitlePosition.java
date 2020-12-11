package org.atpfivt.jsyntrax.styles;

public enum TitlePosition {
    /**
     * Top Left.
     */
    tl {
        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isTop() {
            return true;
        }
    },
    /**
     * Top Middle.
     */
    tm {
        @Override
        public boolean isMiddle() {
            return true;
        }

        @Override
        public boolean isTop() {
            return true;
        }
    },

    /**
     * Top Right.
     */
    tr {
        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public boolean isTop() {
            return true;
        }
    },

    /**
     * Bottom Left.
     */
    bl {
        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isBottom() {
            return true;
        }
    },
    /**
     * Bottom Middle.
     */
    bm {
        @Override
        public boolean isMiddle() {
            return true;
        }

        @Override
        public boolean isBottom() {
            return true;
        }
    },
    /**
     * Bottom Right.
     */
    br {
        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public boolean isBottom() {
            return true;
        }
    };

    public boolean isLeft() {
        return false;
    }

    public boolean isMiddle() {
        return false;
    }

    public boolean isRight() {
        return false;
    }

    public boolean isTop() {
        return false;
    }

    public boolean isBottom() {
        return false;
    }

}
