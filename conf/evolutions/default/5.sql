# Add non-negative constraint

# --- !Ups
ALTER TABLE account ADD CONSTRAINT balance_nonnegative CHECK (a_balance >= 0);

# --- !Downs
ALTER TABLE account DROP CONSTRAINT balance_nonnegative;
