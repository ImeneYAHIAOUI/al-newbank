# newbank-sdk Interface

## `authorizePayment(paymentInformation)`

Envoie une demande d'autorisation de paiement au backend et reçoit un ID de transaction en cas d'acceptation.

**Paramètres:**
- `paymentInformation`: Les informations relatives au paiement (Carte de crédit, montant, etc.)

**Retour:**
- `transactionId`: L'ID de transaction en cas d'acceptation de la demande.

## `confirmPayment(transactionId)`

Envoie une demande de confirmation du paiement pour la transaction préalablement autorisée.

**Paramètres:**
- `transactionId`: L'ID de la transaction à confirmer.

## `pay(paymentInformation)`

Englobe les étapes d'envoi d'une demande d'autorisation de paiement au backend via la méthode `authorizePayment(paymentInformation)` et, en cas d'acceptation, confirme la transaction en utilisant la fonction `confirmPayment(transactionId)`.

**Paramètres:**
- `paymentInformation`: Les informations relatives au paiement.

## `getBackendStatus()`

Envoie une demande de récupération des statuts des services du backend.

**Retour:**
- Statuts des services du backend.
