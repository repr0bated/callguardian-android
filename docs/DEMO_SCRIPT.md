# 3-minute demo: Call Guardian on a Galaxy S25 Ultra

**Setup before recording**
* Stock S25 Ultra. Install `callguardian.apk` (debug build, App Check debug token registered).
* **AICore Developer Preview enrolled:** Settings → Developer options → AICore Settings → Enable AICore Developer Preview → Accept. Update AICore, and confirm the app's Home model card says **Ready · Gemma 4 E2B · Nano 4 Fast**.
* Grant the call screening role, SMS, contacts and notifications. If SMS is greyed out: App info → ⋮ → Allow restricted settings.
* Have a second phone ready to send one scam text, e.g. the USPS redelivery text from the Lab list. Clear history in Settings.
* Recording: Samsung screen recorder (Quick panel → Screen recorder → Media sounds, show touches on).

| Time | On screen | Say |
|---|---|---|
| 0:00–0:15 | Home screen, gradient hero, "Private mode" and model card **Ready** | "This is Call Guardian. It reads my texts and calls for scams on this phone, with Gemma 4 running on-device through Firebase AI Logic." |
| 0:15–0:30 | Pull down the quick panel and turn on **airplane mode**. The hero pill switches to *Offline · on-device* | "I'm going fully offline. No network at all." |
| 0:30–1:05 | Airplane mode means no real SMS can arrive, so open **Lab → Package redelivery**. It runs the identical pipeline. Optional pre-roll: show a real scam text from the second phone that arrived *before* airplane mode, with its alert. The event screen shows the message hidden as **Quarantined** and a **High · Likely scam: Package delivery** verdict with an **On-device · Gemma 4 E2B** pill | "Flagged in under a second, on the device. Look at *How this was decided*: rules first, then Gemma 4 on-device, then its JSON verdict passed `scam_verdict.schema.json`." |
| 1:05–1:35 | Scroll to step 4 *Capability policy*: "proposed quarantine_sms → quarantine_sms (auto)". Tap the receipt row → **Receipt** screen | "The model can't touch my phone. It can only propose one typed method, like `guardian.sms.quarantine@v1`. That gets checked against a JSON Schema and a capability policy, and every run leaves a receipt: input, schema version, where it ran, and the result." |
| 1:35–2:00 | Back on the event, tap **Block number**. The **schema-rendered confirmation sheet** appears with the locked Sender field, Block chips (Calls / Messages / All) and a required Reason with a counter. Tap Run → toast "Done · receipt #N" | "This sheet isn't hand-built. It's rendered at runtime from the method's JSON Schema: required markers, enum chips, read-only bound fields. The schema is the single source of truth for actions and their UI. Add a schema and you get its screens." |
| 2:00–2:40 | Turn **airplane mode off**. Tap **Explain with Gemini**. The consent sheet shows exactly the **redacted** text (`••99`, `[path]`). Tap *Send redacted text*. The deep analysis card fills in: summary, tactics, what to do, model, latency. Then the "Gemini suggests: Report scam" button | "For a deeper look, I opt in. Only a redacted copy goes to cloud Gemini through the same Firebase AI Logic SDK, protected by App Check. Its answer is schema-validated too, and anything it suggests is only offered, never run automatically." |
| 2:40–3:00 | **Receipts** tab: a mix of on-device receipts (quarantine, block) and one `cloud` receipt; tap the Denied filter | "Hybrid by design: private and instant on-device, smart in the cloud when I ask, and every action typed, checked and receipted. Call Guardian, open source under Apache 2.0." |

**Backup shots:** Settings → On-device model choices (Gemma 4 E2B / E4B); Lab samples; a call from a hidden number being silenced (`guardian.call.reject@v1`).
