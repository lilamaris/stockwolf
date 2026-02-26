# private key
openssl genpkey -algorithm RSA -out private/private.pem -pkeyopt rsa_keygen_bits:2048

# public key
openssl rsa -pubout -in private/private.pem -out public/public.pem
