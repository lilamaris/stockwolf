rootProject.name = "stockwolf"

include("kernel")
include("idempotency:core")
include("idempotency:supports:cache:redis")
include("idempotency:supports:store:jpa")

include("event:core")
include("event:supports:kafka")
include("event:supports:jpa")

include("identity")
include("identity-client")

include("inventory:application")
include("inventory:contract")

include("payment:application")
include("payment:contract")