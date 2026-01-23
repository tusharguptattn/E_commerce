package com.ecommerce.ecommerce.enums;

import java.util.Set;

public enum OrderStatus {

    ORDER_PLACED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return Set.of(
                    ORDER_CONFIRMED,
                    ORDER_REJECTED,
                    CANCELLED
            );
        }
    },

    ORDER_CONFIRMED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return Set.of(
                    ORDER_SHIPPED,
                    CANCELLED
            );
        }
    },

    ORDER_SHIPPED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return Set.of(
                    DELIVERED
            );
        }
    },

    DELIVERED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return Set.of(
                    RETURN_REQUESTED,
                    CLOSED
            );
        }
    },

    RETURN_REQUESTED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return Set.of(
                    RETURN_APPROVED,
                    RETURN_REJECTED
            );
        }
    },

    RETURN_APPROVED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return Set.of(
                    PICK_UP_INITIATED
            );
        }
    },

    RETURN_REJECTED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return Set.of(CLOSED);
        }
    },

    PICK_UP_INITIATED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return Set.of(
                    PICK_UP_COMPLETED
            );
        }
    },

    PICK_UP_COMPLETED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return Set.of(
                    REFUND_INITIATED
            );
        }
    },

    REFUND_INITIATED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return Set.of(
                    REFUND_COMPLETED
            );
        }
    },

    REFUND_COMPLETED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return Set.of(CLOSED);
        }
    },

    ORDER_REJECTED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return Set.of(
                    REFUND_INITIATED,
                    CLOSED
            );
        }
    },

    CANCELLED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return Set.of(
                    REFUND_INITIATED,
                    CLOSED
            );
        }
    },


    CLOSED {
        @Override
        public Set<OrderStatus> allowedNext() {
            return Set.of(); // terminal state
        }
    };

    // 👇 abstract method
    public abstract Set<OrderStatus> allowedNext();

    // 👇 common helper
    public boolean canTransitionTo(OrderStatus next) {
        return allowedNext().contains(next);
    }
}
