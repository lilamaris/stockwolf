rootProject.name = "stockwolf"

include("kernel")
include("idempotency:idempotency-core")
include("idempotency:idempotency-support-redis")
include("idempotency:idempotency-support-jpa")

include("event:event-core")
include("event:event-support-jpa")

include("identity:identity-application")
include("identity:identity-client")

include("inventory:inventory-application")
include("inventory:inventory-contract")

include("payment:payment-application")
include("payment:payment-contract")

include("order:order-application")
include("order:order-contract")
