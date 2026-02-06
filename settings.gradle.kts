rootProject.name = "stockwolf"

include("kernel")
include("idempotency:core")
include("idempotency:supports:cache:redis")
include("idempotency:supports:store:jpa")

include("identity")
include("identity-client")

include("inventory")
include("payment")