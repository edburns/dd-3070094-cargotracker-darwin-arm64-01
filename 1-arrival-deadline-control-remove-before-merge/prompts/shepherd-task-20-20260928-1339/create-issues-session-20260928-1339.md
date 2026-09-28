# Copilot CLI Session

> [!NOTE]
> - **Session ID:** `83115350-6d8e-4058-8dbf-7431a260eb3b`  
> - **Started:** 9/28/2026, 1:39:21 PM  
> - **Duration:** 3m 3s  
> - **Exported:** 9/28/2026, 1:42:24 PM  

---

<sub>2s</sub>

### User

Invoke skill `shepherd-task-20-create-issues-from-plan` with these inputs:

- CAMPAIGN_ID: e87e808f-d031-4520-a1e5-6330b5749371
- LESSON_PROPAGATION: off
- REPO: edburns/dd-3070094-cargotracker-darwin-arm64-01
- BASE_BRANCH: experiment/shepherd-control
- PARENT_ISSUE: 1
- PLAN_DIRECTORY: 1-arrival-deadline-control-remove-before-merge
- PLAN_FILE_NAME: add-change-arrival-deadline-feature-ignorance-reduction-plan.md
- QUESTIONS_SECTION: ## Phase 3 — Ignorance reduction: questions to answer before writing code
- IMPLEMENTATION_SECTION: ## Phase 4 — Implementation (five serial issues)
- EXPECTED_TASK_COUNT: 5
- BASE_REMOTE: origin
- LOG_DIRECTORY: /Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339
- DRAFT_VALIDATOR: /Users/edburns/.copilot/plugins/shepherd-task/scripts/validate-stage20-drafts.sh
- ISSUE_BODY_VERIFIER: /Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-github-issue-body.sh
- CHILD_LINK_VERIFIER: /Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-stage20-child-links.sh

Fixture pagination response contract (mandatory):

- `gh api ... --paginate --slurp` returns a JSON array of page payloads, so a
  one-page response has the shape `[[{...}]]`, not `[{...}]`.
- Before indexing child issue fields such as `.id`, normalize the response to
  one flat issue array exactly once.
- In Bash, use:
  `jq 'if length == 0 then [] elif all(.[]; type == "array") then add else . end'`.
- In PowerShell, capture the `gh` output and `$LASTEXITCODE` first, then pass
  the complete JSON through the same `jq` normalization before
  `ConvertFrom-Json`.
- Use the normalized flat array for the pre-creation baseline, final child
  count/order checks, and failure reconciliation. Do not apply `add` a second
  time to an already-flat array.

---

<sub>5s</sub>

### `skill`

**shepherd-task-20-create-issues-from-plan**

<details>
<summary>Arguments</summary>

```json
{
  "skill": "shepherd-task-20-create-issues-from-plan"
}
```

</details>

```
Skill "shepherd-task-20-create-issues-from-plan" loaded successfully. Follow the instructions in the skill context.
```


---

<sub>8s</sub>

### Copilot

I’m validating the campaign inputs, plan structure, repository state, and required tooling before drafting any issue bodies or mutating GitHub.

---

<sub>18s</sub>

### `bash`

**Validate directories and scripts**

$ set -e
LOG_DIRECTORY='/Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339'
mkdir -p "$LOG_DIRECTORY/issue-bodies"
test -d "$LOG_DIRECTORY" && test -w "$LOG_DIRECTORY"
for script in \
'/Users/edburns/.copilot/plugins/shepherd-task/scripts/validate-stage20-drafts.sh' \
'/Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-github-issue-body.sh' \
'/Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-stage20-child-links.sh'; do
  test -f "$script" && test -x "$script"
done
printf 'local-preflight-ok\n'

```
local-preflight-ok
<shellId: 0 completed with exit code 0>
```


---

<sub>18s</sub>

### `bash`

**Validate GitHub repository inputs**

$ set -e
REPO='edburns/dd-3070094-cargotracker-darwin-arm64-01'
BASE_BRANCH='experiment/shepherd-control'
BASE_REMOTE='origin'
default_branch="$(gh api "repos/$REPO" --jq '.default_branch')"
printf 'default_branch=%s\n' "$default_branch"
test "$BASE_BRANCH" != main
test "$BASE_BRANCH" != "$default_branch"
git show-ref --verify --quiet "refs/heads/$BASE_BRANCH" || git show-ref --verify --quiet "refs/remotes/$BASE_REMOTE/$BASE_BRANCH"
remote_url="$(git remote get-url "$BASE_REMOTE")"
printf 'remote_url=%s\n' "$remote_url"
case "$remote_url" in
  *github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01.git|*github.com:edburns/dd-3070094-cargotracker-darwin-arm64-01.git) ;;
  *) printf 'remote mismatch\n' >&2; exit 1;;
esac
gh api "repos/$REPO/issues/1" --jq '{number,state,title,repository_url}'
repo_json="$(gh api "repos/$REPO")"
owner_type="$(jq -r '.owner.type' <<<"$repo_json")"
owner_login="$(jq -r '.owner.login' <<<"$repo_json")"
printf 'owner=%s owner_type=%s\n' "$owner_login" "$owner_type"
if [ "$owner_type" = Organization ]; then
  gh api "orgs/$owner_login/issue-types" --jq '.[] | select(.name == "Task" and .is_enabled == true) | .name'
elif [ "$owner_type" != User ]; then
  exit 1
fi

```
default_branch=20260902-2104Z-commit-e7b651f-liberty
remote_url=git@github.com:edburns/dd-3070094-cargotracker-darwin-arm64-01.git
{"number":1,"repository_url":"https://api.github.com/repos/edburns/dd-3070094-cargotracker-darwin-arm64-01","state":"open","title":"[Campaign][off] Cargo Tracker arrival-deadline control"}
owner=edburns owner_type=User
<shellId: 1 completed with exit code 0>
```


---

<sub>18s</sub>

### `bash`

**Read plan and campaign manifest**

$ set -e
BRANCH='experiment/shepherd-control'
PLAN='1-arrival-deadline-control-remove-before-merge/add-change-arrival-deadline-feature-ignorance-reduction-plan.md'
MANIFEST='1-arrival-deadline-control-remove-before-merge/shepherd-campaign.json'
LESSONS='1-arrival-deadline-control-remove-before-merge/campaign-lessons.md'
git show "$BRANCH:$PLAN"
printf '\n---CAMPAIGN-MANIFEST---\n'
git show "$BRANCH:$MANIFEST"
printf '\n---LESSONS-EXISTS---\n'
git cat-file -e "$BRANCH:$LESSONS" && printf 'yes\n'

<details>
<summary>11 lines</summary>

```
Output too large to read at once (34.1 KB). Saved to: /var/folders/vc/_9vlsvsd7wg72zf3bf0z_1k00000gn/T/1790617178741-copilot-tool-output-30241-53076239-e079-454c-9905-889dc40544aa.txt
Consider using tools like rg (for searching), head/tail (for viewing start/end), view with view_range (for specific sections), or jq (for JSON) to examine portions of the output.

Preview (first 500 chars):
# Implementation plan: Change Arrival Deadline Date (`eclipse-ee4j/cargotracker#64`)

Human DRI: Ed Burns
Starting commit: `9b9f311b2a3a2854bdac947593950d9edb6bca7d` (`Make the system ready for implementation`)
Working directory: repository root of the current campaign worktree
Runtime baseline: Java 17, Java EE 7 (`javax.*`), Open Liberty 26.0.0.8, PrimeFaces 8.0
Baseline run instructions: `README.md`
Baseline preparation: fixed source branch and immutable SHA validated by the campaign fixture

<shellId: 2 completed with exit code 0>
```

</details>


---

<sub>18s</sub>

### `view`

**view the file at /Users/edburns/.copilot/skills/shepherd-task-20-create-issues-from-plan/examples/01-1682-java-tool-ergonomics.md.**

/Users/edburns/.copilot/skills/shepherd-task-20-create-issues-from-plan/examples/01-1682-java-tool-ergonomics.md

<details>
<summary>15 lines</summary>

```
# Example 01: 1682-java-tool-ergonomics-prompts-remove-before-merge

Read `copilot-sdk/1682-java-tool-ergonomics-prompts-remove-before-merge/dd-3018003-ignorance-reduction-for-implementation-plan.md` (the plan) to get the context.

✅✅Pay very close attention to each **Resolution:** to the questions in `Phase 3 — Ignorance reduction: questions to answer before writing code`.

In this session, we are creating issues of type **Task** as children of Epic https://github.com/github/copilot-sdk/issues/1682 . These issues correspond to the sub-sections in `## Phase 4 — Implementation (the build order)`.

I want you to use `gh` to create issues as sub-issues of 1682.

- I will dispatch these issues by assigning them to an agent. Leave them unassigned for now.

- The description you put in the issues you create is the specification for the work the agent assigned to the issue must do. Therefore, you need to be very specific and targeted when writing the description.

- Set the type to **Task**.

- Each issue will be worked on and resolved in serial. You can assume:
  - All issues will operate in this topic branch of `upstream`: `edburns/1682-java-tool-ergonomics`.

  - The assignee agent has access to the topic branch and, most importantly, to the prompts in the directory `1682-java-tool-ergonomics-prompts-remove-before-merge`, which includes the plan. Therefore, you can instruct the agent to access the plan by reference, you don't need to reference it by value directly in the issue.

- The sub-sections of `## Phase 4 — Implementation (the build order)` have essential details for each corresponding **Task**.

- Instruct the agent to read the entire plan before working.

- Instruct the agent to carefully re-read the relevant sections of the plan. ❌❌You can't just say, "read the relevant sections of the plan". ✅✅In the description of the issue, list exactly what sections of the plan apply to this particular issue.
- Though the sub-sections of `## Phase 4 — Implementation (the build order)` have essential details for each corresponding **Task**, I want you to carefully consider if there are additional gating tests you can add so that as the agent proceeds from one task to the next, the likelihood of rework is reduced.
```

</details>


---

<sub>18s</sub>

### `view`

**view the file at /Users/edburns/.copilot/skills/shepherd-task-20-create-issues-from-plan/examples/02-1810-java-tool-as-lambda.md.**

/Users/edburns/.copilot/skills/shepherd-task-20-create-issues-from-plan/examples/02-1810-java-tool-as-lambda.md

<details>
<summary>12 lines</summary>

```
# Example 02: 1810-java-tool-ergonomics-tool-as-lambda-remove-before-merge

Carefully read the plan `copilot-sdk/1810-java-tool-ergonomics-tool-as-lambda-remove-before-merge/1810-ignorance-reduction-for-implementation-plan.md`.

We have completed Phases 1 - 3. We are now on Phase 4.

For each sub item in Phase 4, file a child issue of Feature #1810.

Use `gh` to look at Epic #1682 and its child issues for the standard of verbosity and concreteness for issue descriptions.

The issue should be assignable to a coding agent and there must be sufficient detail in the description for the agent to proceed to completion.

Pull out the specific relevant content from Phase 3 as it applies to this current issue in Phase 4.

Tell the agent to take `edburns/1810-java-tool-ergonomics-tool-as-lambda` as the base branch. When I assign the issue to the coding agent, I will set this as the base branch.

Tell the agent to read `1810-java-tool-ergonomics-tool-as-lambda-remove-before-merge/1810-ignorance-reduction-for-implementation-plan.md`, which is present on the branch, for context.

Tell the agent which sub-item within Phase 4 they are solving.

Tell the agent that each issue will be assigned, resolved and merged to the base branch in the order in which they appear in the plan.

No work is to start until I assign each issue to the coding agent, in turn.
```

</details>


---

<sub>18s</sub>

### `view`

**view the file at /Users/edburns/.copilot/skills/shepherd-task-20-create-issues-from-plan/examples/03-dd-3017826-java-real-estate-demo.md.**

/Users/edburns/.copilot/skills/shepherd-task-20-create-issues-from-plan/examples/03-dd-3017826-java-real-estate-demo.md

<details>
<summary>14 lines</summary>

```
# Example 03: dd-3017826-java-real-estate-demo-remove-before-merge

Read `dd-3017826-java-real-estate-demo-remove-before-merge\dd-3017826-java-real-estate-demo-ignorance-reduction-plan.md` (the plan) to get the context.

✅✅Pay very close attention to each **Resolution:** to the questions in `Phase 2 — Ignorance reduction: questions to answer before writing code`.

In this session, we are creating sub-issues as children of https://github.com/edburns/Build26-BRK206-your-agent-anywhere-multiclient-multidevice-with-github-copilot-sdk/issues/2 . These issues correspond to the sub-sections in `## Phase 3 — Implementation (build order)`.

I want you to use `gh` to create issues as sub-issues of #2.

- I will dispatch these issues by assigning them to an agent. Leave them unassigned for now.

- The description you put in the issues you create is the specification for the work the agent assigned to the issue must do. Therefore, you need to be very specific and targeted when writing the description.

- Each issue will be worked on and resolved in serial. You can assume:
  - All issues will operate in this topic branch of `origin`: `edburns/2-build-out-demo`.

  - The assignee agent has access to the topic branch and, most importantly, to the prompts in the directory `dd-3017826-java-real-estate-demo-remove-before-merge`, which includes the plan. Therefore, you can instruct the agent to access the plan by reference, you don't need to reference it by value directly in the issue.

- The sub-sections of `## Phase 3 — Implementation (build order)` have essential details for each corresponding sub-issue.

- Instruct the agent to read the entire plan before working.

- Instruct the agent to carefully re-read the relevant sections of the plan. ❌❌You can't just say, "read the relevant sections of the plan". ✅✅In the description of the issue, list exactly what sections of the plan apply to this particular issue.
- Though the sub-sections of `## Phase 3 — Implementation (build order)` have essential details for each corresponding sub-issue, I want you to carefully consider if there are additional gating tests you can add so that as the agent proceeds from one task to the next, the likelihood of rework is reduced.
```

</details>


---

<sub>18s</sub>

### `view`

**view the file at /Users/edburns/.copilot/skills/shepherd-task-20-create-issues-from-plan/examples/04-28-python-agent-demo.md.**

/Users/edburns/.copilot/skills/shepherd-task-20-create-issues-from-plan/examples/04-28-python-agent-demo.md

<details>
<summary>16 lines</summary>

```
# Example 04: 28-python-agent-demo-remove-before-merge

Read `28-python-agent-demo-remove-before-merge/28-python-agent-demo-ignorance-reduction-plan.md` (the plan) to get the context.

✅✅Pay very close attention to each **Resolution:** to the questions in `Phase 2 — Ignorance reduction: questions to answer before writing code`. These pertain to the implementation tasks in Phase 3. ✅✅✅ Include relevant spike findings as self-contained prose in the issue description; do not direct the agent to spike directories or source files.

In this session, we are creating sub-issues as children of https://github.com/edburns/Build26-BRK206-your-agent-anywhere-multiclient-multidevice-with-github-copilot-sdk/issues/28 . These issues correspond to the sub-sections in `## Phase 3 — Implementation (build order)`.

I want you to use `gh` to create issues as sub-issues of #28

- I will dispatch these issues by assigning them to an agent. Leave them unassigned for now.

- The description you put in the issues you create is the specification for the work the agent assigned to the issue must do. Therefore, you need to be very specific and targeted when writing the description.

- Again, ✅✅For each **Resolution:** to the questions in `Phase 2 — Ignorance reduction: questions to answer before writing code`, include the relevant Resolution values in the issue description. These pertain to the implementation tasks in Phase 3. ✅✅✅ State relevant spike findings directly in the issue body with enough detail to implement them without reading spike code.

- For tasks dealing with the UI, make sure to refer to the relevant screengrabs, but only ones within `28-python-agent-demo-remove-before-merge/`.

- Each issue will be worked on and resolved in serial. You can assume:
  - All issues will operate in this topic branch of `origin`: `edburns/28-python-agent-demo`.

  - The assignee agent has access to the topic branch and, most importantly, to the prompts in the directory `28-python-agent-demo-remove-before-merge/`, which includes the plan. Therefore, you can instruct the agent to access the plan by reference, you don't need to reference it by value directly in the issue.

- The sub-sections of `## Phase 3 — Implementation (build order)` have essential details for each corresponding sub-issue.

- Instruct the agent to read the entire plan before working.

- Instruct the agent to carefully re-read the relevant sections of the plan. ❌❌You can't just say, "read the relevant sections of the plan". ✅✅In the description of the issue, list exactly what sections of the plan apply to this particular issue.
- Though the sub-sections of `## Phase 3 — Implementation (build order)` have essential details for each corresponding sub-issue, I want you to carefully consider if there are additional gating tests you can add so that as the agent proceeds from one task to the next, the likelihood of rework is reduced.
```

</details>


---

<sub>18s</sub>

### `bash`

**Read existing parent children**

$ set -e
REPO='edburns/dd-3070094-cargotracker-darwin-arm64-01'
OUT="$(gh api "repos/$REPO/issues/1/sub_issues" --paginate --slurp)"
printf '%s\n' "$OUT" | jq 'if length == 0 then [] elif all(.[]; type == "array") then add else . end'

```
[]
<shellId: 3 completed with exit code 0>
```


---

<sub>23s</sub>

### Copilot

Preflight passed: the base branch is valid, the parent is open, the owner is a user (so children will be untyped), and there are currently no child issues. I’m now extracting every resolution, implementation subsection, and referenced research finding into the five draft specifications.

---

<sub>26s</sub>

### `bash`

**Extract resolved questions**

$ git show 'experiment/shepherd-control:1-arrival-deadline-control-remove-before-merge/add-change-arrival-deadline-feature-ignorance-reduction-plan.md' | awk 'BEGIN{p=0} /^## Phase 3 — Ignorance reduction: questions to answer before writing code$/{p=1} /^## Phase 4 — Implementation \(five serial issues\)$/{if(p){exit}} p{print}'

<details>
<summary>275 lines</summary>

````
## Phase 3 — Ignorance reduction: questions to answer before writing code

Resolve these questions before production implementation begins. The
recommendations intentionally define the desired design closely enough that an
implementing agent should not need to invent a different architecture.

### 3.1 — Which cargos expose the edit operation?

**Question:** Should deadline editing be exposed for all cargos or only for
cargos displayed in the Not Routed Cargo table?

The requested feature originates in the Administration dashboard's Not Routed
Cargo table. Other tables represent routed, misrouted, claimed, or otherwise
progressed cargo. Adding the affordance to every table would expand the feature
and require additional business rules about changing deadlines after handling
has begun.

| Option | UI scope | Trade-off |
|--------|----------|-----------|
| A | Not Routed Cargo table only | Matches the requested feature and the established destination-edit affordance. |
| B | Every Administration cargo table | Broader capability, but introduces lifecycle and authorization questions outside the request. |
| C | Cargo details page only | Avoids table complexity but does not meet the requested dashboard interaction. |

The application-service operation itself does not need to encode a UI-table
restriction. It should accept a tracking ID and apply the domain mutation to
the located cargo. The presentation layer determines where the operation is
offered.

**Recommendation:** Option A. Add the edit affordance only to
`src/main/webapp/admin/tables/listNotRouted.xhtml`. Keep the application
operation generally usable for a valid cargo.

**Resolution:**

Select Option A. Expose the edit affordance only in
`src/main/webapp/admin/tables/listNotRouted.xhtml`. The application and facade
operations remain generally callable for any cargo that can be found by
tracking ID; they do not encode knowledge of dashboard table membership.

### 3.2 — What is the exact domain mutation?

**Question:** Should the feature mutate the existing `RouteSpecification`, add
a setter to `Cargo`, or replace the specification using the existing domain
operation?

`RouteSpecification` is a value object describing origin, destination, and
arrival deadline. The existing `changeDestination(...)` implementation already
establishes the correct pattern: create a replacement specification, call
`Cargo.specifyNewRoute(...)`, and store the aggregate.

Proposed application-service shape:

```java
void changeDeadline(TrackingId trackingId, Date deadline);
```

Proposed implementation:

```java
Cargo cargo = cargoRepository.find(trackingId);
RouteSpecification specification = new RouteSpecification(
        cargo.getOrigin(),
        cargo.getRouteSpecification().getDestination(),
        deadline);

cargo.specifyNewRoute(specification);
cargoRepository.store(cargo);
```

Calling `specifyNewRoute(...)` is significant. It lets the aggregate recalculate
delivery and routing status relative to the new specification. Direct field
mutation or a persistence-only update would bypass that behavior.

**Recommendation:** Replace the `RouteSpecification` through
`Cargo.specifyNewRoute(...)`. Preserve origin, destination, and itinerary.
Persist using the existing repository. Do not add a deadline setter to the
domain model.

**Resolution:**

Use the same aggregate-update pattern as `changeDestination(...)`. Add
`BookingService.changeDeadline(TrackingId, Date)` and implement it by loading
the cargo, constructing a new `RouteSpecification` from the existing origin,
existing destination, and supplied deadline, calling
`cargo.specifyNewRoute(...)`, and storing the cargo through
`cargoRepository.store(...)`. Do not add mutable deadline setters to the domain
objects.

### 3.3 — What should happen to an existing itinerary and delivery state?

**Question:** When a routed cargo's deadline changes, should its itinerary be
cleared, retained, or recomputed?

Although the UI initially exposes the feature only for unrouted cargo, the
application operation should have deterministic domain behavior if invoked for
a routed cargo. The existing `changeDestination(...)` behavior preserves the
assigned itinerary and lets `Cargo.specifyNewRoute(...)` recalculate whether
that itinerary still satisfies the new specification.

The core application test should deliberately invoke the operation after:

1. booking a cargo;
2. requesting route candidates;
3. assigning an itinerary;
4. changing its destination;
5. changing its deadline.

This sequence verifies that the feature uses the aggregate correctly rather
than assuming the cargo always has an empty itinerary.

**Recommendation:** Preserve the itinerary. Let the domain model recompute
routing and delivery-derived state. Assert all unaffected fields explicitly in
`BookingServiceTest`.

**Resolution:**

Retain the existing itinerary. Do not clear, replace, or reroute it as part of
the deadline change. `Cargo.specifyNewRoute(...)` recalculates the delivery
snapshot and routing status against the replacement specification. In the
established sequential application test, the assigned itinerary remains
unchanged and the cargo remains `MISROUTED` after the deadline changes.

### 3.4 — What type crosses the facade boundary?

**Question:** Should the booking facade accept a `Date`, a formatted string, or
a newly introduced request DTO?

The existing facade already uses `java.util.Date` for
`bookNewCargo(...)`. Introducing another representation for this one operation
would create unnecessary conversion code and depart from the historical
application style.

Proposed facade shape:

```java
void changeDeadline(String trackingId, Date arrivalDeadline);
```

The implementation converts only the identifier:

```java
bookingService.changeDeadline(
        new TrackingId(trackingId),
        arrivalDeadline);
```

**Recommendation:** Use `String` for the tracking ID and `java.util.Date` for
the deadline. Do not expose `TrackingId`, `Cargo`, or `RouteSpecification` to
the JSF layer and do not introduce a new DTO solely for this command.

**Resolution:**

Add `void changeDeadline(String trackingId, Date arrivalDeadline)` to
`BookingServiceFacade`. `DefaultBookingServiceFacade` converts the string to
`new TrackingId(trackingId)` and passes the same `Date` to
`BookingService.changeDeadline(...)`. No new command DTO or formatted-string
service parameter is introduced.

### 3.5 — How is the DTO's formatted deadline converted for editing?

**Question:** `CargoRoute` exposes its deadline as formatted strings, while
`p:datePicker` binds naturally to `java.util.Date`. How should the backing bean
initialize the editor?

At the starting commit:

- `CargoRoute.getArrivalDeadline()` returns
  `MM/dd/yyyy hh:mm a z`.
- `CargoRoute.getArrivalDeadlineDate()` returns only the date component.
- The table displays `getArrivalDeadlineDate()`.

Options:

| Option | Approach | Trade-off |
|--------|----------|-----------|
| A | Parse `cargo.getArrivalDeadlineDate()` with `MM/dd/yyyy` | Small, localized change; preserves the existing DTO contract. |
| B | Add a `Date` property to `CargoRoute` | Cleaner typing, but broadens a DTO used throughout the application. |
| C | Reload the domain object in the backing bean | Violates the facade boundary. |

The formatter/parser must be created per operation or per view bean; do not add
a shared mutable `SimpleDateFormat`.

**Recommendation:** Option A. Load `CargoRoute` through
`BookingServiceFacade.loadCargoForRouting(trackingId)` and parse
`cargo.getArrivalDeadlineDate()` using `new SimpleDateFormat("MM/dd/yyyy")`.
Surface an explicit failure if the existing DTO value cannot be parsed; do not
silently submit a null date.

**Resolution:**

Use Option A and keep date conversion inside the view-scoped editor bean. The
existing implementation loads the `CargoRoute`, creates
`new SimpleDateFormat("MM/dd/yyyy")`, and parses the leading date portion of
`cargo.getArrivalDeadline()`. Because that value begins with `MM/dd/yyyy`,
`SimpleDateFormat.parse(...)` obtains the same date that
`getArrivalDeadlineDate()` displays. A per-load formatter is used, so no shared
mutable formatter is added.

### 3.6 — Which JSF bean scopes and interaction pattern should be used?

**Question:** Should deadline editing introduce a new navigation page, use an
inline editor, or mirror the existing Change Destination dynamic-dialog
pattern?

The baseline already contains:

- `ChangeDestination`, a CDI `@Named` and JSF `@ViewScoped` editor bean;
- `ChangeDestinationDialog`, a session-scoped JSF managed bean that opens and
  closes a PrimeFaces dynamic dialog;
- `changeDestination.xhtml`, a dialog view;
- a `dialogReturn` Ajax listener that refreshes `tableNotRouted`.

Using the same pattern minimizes changes and provides a consistent user
experience.

Proposed bean names:

```text
changeArrivalDeadlineDate
changeArrivalDeadlineDateDialog
```

**Recommendation:** Add a serializable CDI `@Named @ViewScoped`
`ChangeArrivalDeadlineDate` editor and a serializable
`@ManagedBean(name = "changeArrivalDeadlineDateDialog") @SessionScoped`
launcher. Mirror the existing destination-dialog lifecycle rather than
introducing a new navigation or inline-edit framework.

**Resolution:**

Mirror the existing Change Destination interaction. Implement
`ChangeArrivalDeadlineDate` as a serializable CDI `@Named @ViewScoped` bean and
`ChangeArrivalDeadlineDateDialog` as a serializable
`@ManagedBean(name = "changeArrivalDeadlineDateDialog") @SessionScoped` bean.
Use a PrimeFaces dynamic dialog rather than navigation to a full page or inline
cell editing.

### 3.7 — What is the dynamic-dialog contract?

**Question:** What path, request parameters, dimensions, and close result should
the PrimeFaces dialog use?

The launcher needs one parameter, `trackingId`, supplied as a
`Map<String, List<String>>`. The dialog metadata binds the parameter and invokes
the editor bean's `load()` action.

Proposed launcher contract:

```java
PrimeFaces.current().dialog().openDynamic(
        "/admin/dialogs/changeArrivalDeadlineDate.xhtml",
        options,
        params);
```

Required options:

| Option | Value |
|--------|-------|
| `modal` | `true` |
| `draggable` | `true` |
| `resizable` | `false` |
| `contentWidth` | `410` |
| `contentHeight` | `280` |

Required completion behavior:

- successful update: `closeDynamic("DONE")`;
- cancel: `closeDynamic("")`;
- caller listens for `dialogReturn` and updates `tableNotRouted`.

Because Open Liberty uses MyFaces, `<f:metadata>` must be a direct child of the
view root, before `<h:head>` and `<h:body>`. It must not be nested inside
`<h:body>`.

**Recommendation:** Use the contract above and preserve the metadata placement
required by the prepared baseline.

**Resolution:**

Open `/admin/dialogs/changeArrivalDeadlineDate.xhtml` with a single
`trackingId` request parameter and these options: modal and draggable are
`true`, resizable is `false`, content width is `410`, and content height is
`280`. Successful submission closes with `"DONE"`; cancellation closes with
the empty string. The caller handles `dialogReturn` and updates
`tableNotRouted`. Place the dialog's `<f:metadata>` directly under the root
`<html>` element, before `<h:head>` and `<h:body>`, so the known MyFaces
`UIViewRoot` requirement is satisfied.

### 3.8 — What date validation is required?

**Question:** Must the new deadline be non-null, in the future, after the
current date, or after itinerary completion?

The requested feature is an administrative correction to an existing arrival
deadline. No new domain policy about future dates is part of the request.
Inventing such a rule could reject dates accepted by existing cargo booking or
`RouteSpecification` behavior.

The UI must nevertheless prevent a null submission because the operation
requires a concrete replacement deadline.

**Recommendation:** Require a date value in the JSF form and display a normal
Faces validation message when it is absent. Do not add a new minimum-date,
future-date, or itinerary-date business rule. Continue to rely on the existing
domain model for its established invariants.

**Resolution:**

Require a non-null date selection, but add no new chronological business rule.
In particular, do not require the replacement deadline to be after today,
after the old deadline, or after every itinerary leg. Pass the selected
`java.util.Date` to the existing domain construction path and let the current
`RouteSpecification` invariants apply.

### 3.9 — How will the feature be tested on the prepared historical baseline?

**Question:** Which automated and runtime tests are mandatory, given that the
historical JUnit/Arquillian suite is configured for a remote Payara 4
container, while the prepared production baseline runs on JDK 17/Open Liberty?

The starting POM deliberately leaves `skipTests=true`. The Open Liberty profile
builds and compiles all test sources but does not provide a Liberty Arquillian
adapter. Modernizing the entire integration-test runtime is outside this
feature's scope.

The feature still needs layered evidence:

1. Extend `BookingServiceTest` with the domain/application assertions that
   specify the deadline mutation.
2. Ensure all test sources compile as part of
   `./mvnw clean package -Popenliberty`.
3. Add focused JUnit tests for facade and backing-bean delegation where they
   can run without a container, using hand-written fakes rather than adding a
   mocking framework.
4. Perform mandatory end-to-end verification against the running Open Liberty
   application.
5. Preserve the existing Payara Arquillian test path; do not delete, disable,
   or rewrite it to manufacture a passing result.

**Spike needed:** Before Issue 1 implementation, run the starting commit's
standard Open Liberty package command and record whether tests are compiled but
skipped. Confirm the new `BookingServiceTest` method can be added without
expanding the runtime modernization scope.

**Recommendation:** Treat the JDK 17/Open Liberty build plus HTTP/UI acceptance
as the mandatory executable gate. Keep the historical Arquillian test as a
precise application-layer specification and run it only when its documented
Payara environment is available.

**Resolution:**

Extend the existing sequential Arquillian `BookingServiceTest` with
`testChangeDeadline()` after `testChangeDestination()`. The test changes the
deadline by one month, reloads the cargo through JPA, and asserts the complete
set of preserved and recalculated domain state described above. The prepared
Open Liberty build compiles this test but retains the historical default
`skipTests=true`; executing that Arquillian suite still requires its documented
remote Payara environment. Therefore the mandatory executable gates are the
JDK 17 Open Liberty package/start command, direct HTTP checks, and the complete
`DEF789` browser acceptance flow. No Arquillian-runtime modernization or new
mocking dependency is part of this feature.

---
<shellId: 5 completed with exit code 0>
````

</details>


---

<sub>26s</sub>

### `bash`

**Extract implementation tasks**

$ git show 'experiment/shepherd-control:1-arrival-deadline-control-remove-before-merge/add-change-arrival-deadline-feature-ignorance-reduction-plan.md' | awk 'BEGIN{p=0} /^## Phase 4 — Implementation \(five serial issues\)$/{p=1} /^## Phase 5/{if(p){exit}} p{print}'

<details>
<summary>281 lines</summary>

````
## Phase 4 — Implementation (five serial issues)

Implement these issues in order. Each issue should be a separate commit. Do not
start an issue until the previous issue's gating criteria are satisfied.

### 4.1 — Issue 1: Add the application-layer deadline change operation

**What to build**

Add the core use case to the application layer. This issue must contain no JSF
or PrimeFaces changes.

**Files to modify**

- `src/main/java/org/eclipse/cargotracker/application/BookingService.java`
- `src/main/java/org/eclipse/cargotracker/application/internal/DefaultBookingService.java`
- `src/test/java/org/eclipse/cargotracker/application/BookingServiceTest.java`

**Required API**

```java
void changeDeadline(TrackingId trackingId, Date deadline);
```

**Required implementation behavior**

1. Load the cargo using `cargoRepository.find(trackingId)`.
2. Obtain the current destination from
   `cargo.getRouteSpecification().getDestination()`.
3. Construct a replacement `RouteSpecification` from:
   - `cargo.getOrigin()`;
   - the current destination;
   - the new deadline.
4. Apply it using `cargo.specifyNewRoute(routeSpecification)`.
5. Persist using `cargoRepository.store(cargo)`.
6. Log the tracking ID and new deadline at `Level.INFO`, following the style of
   `changeDestination(...)`.

Do not:

- add a setter to `Cargo` or `RouteSpecification`;
- modify the origin or destination;
- clear or replace the itinerary directly;
- update persistence entities behind the aggregate's back.

**Tests to write first**

Append a sequential `testChangeDeadline()` case to `BookingServiceTest` after
`testChangeDestination()`. Build a new deadline one month after the test's
original `deadline`, invoke the service, reload the cargo with
`Cargo.findByTrackingId`, and assert:

- origin remains Chicago;
- destination remains Helsinki;
- stored deadline is the same calendar day as the requested new deadline;
- assigned itinerary remains unchanged;
- transport status remains `NOT_RECEIVED`;
- last known location remains `Location.UNKNOWN`;
- current voyage remains `Voyage.NONE`;
- cargo is not marked misdirected;
- estimated time of arrival is `Delivery.ETA_UNKOWN`;
- next expected activity is `Delivery.NO_ACTIVITY`;
- cargo is not unloaded at destination;
- routing status reflects the domain model's recalculation and remains
  `MISROUTED` for the established test sequence.

**Gating criteria**

- The test source compiles.
- `./mvnw clean package -Popenliberty` succeeds on JDK 17.
- No web, facade, REST, Liberty, or persistence configuration files change in
  this issue.

### 4.2 — Issue 2: Expose deadline changes through the booking facade

**What to build**

Expose the use case to presentation clients without leaking domain identifier
types into the web layer.

**Files to modify**

- `src/main/java/org/eclipse/cargotracker/interfaces/booking/facade/BookingServiceFacade.java`
- `src/main/java/org/eclipse/cargotracker/interfaces/booking/facade/internal/DefaultBookingServiceFacade.java`

**Optional focused test file**

- `src/test/java/org/eclipse/cargotracker/interfaces/booking/facade/internal/DefaultBookingServiceFacadeTest.java`

**Required API**

```java
void changeDeadline(String trackingId, Date arrivalDeadline);
```

**Required implementation**

```java
bookingService.changeDeadline(
        new TrackingId(trackingId),
        arrivalDeadline);
```

The facade must not:

- load and mutate `Cargo` itself;
- call `CargoRepository.store(...)`;
- parse a formatted date;
- introduce JSF or PrimeFaces types.

**Tests**

Where a container-free test is added, use a hand-written `BookingService` fake
or spy and prove that:

- the same `Date` object/value reaches the application service;
- the tracking-ID string is converted to an equivalent `TrackingId`;
- the facade delegates exactly once;
- no repository work is duplicated in the facade.

Do not add Mockito or another dependency solely for this test.

**Gating criteria**

- Existing facade consumers still compile.
- `./mvnw clean package -Popenliberty` succeeds on JDK 17.
- The application-layer test added in Issue 1 remains unchanged and compiling.

### 4.3 — Issue 3: Implement the deadline editor backing model

**What to build**

Add the view-scoped backing bean that loads a cargo's current deadline and
submits a replacement deadline through the booking facade. Do not add the
dialog launcher or XHTML in this issue.

**File to create**

- `src/main/java/org/eclipse/cargotracker/interfaces/booking/web/ChangeArrivalDeadlineDate.java`

**Required bean shape**

```java
@Named
@ViewScoped
public class ChangeArrivalDeadlineDate implements Serializable {
    private static final long serialVersionUID = 1L;

    private String trackingId;
    private CargoRoute cargo;
    private Date arrivalDeadlineDate;

    @Inject
    private BookingServiceFacade bookingServiceFacade;
}
```

Required properties and methods:

- `getTrackingId()` / `setTrackingId(String)`
- `getCargo()`
- `getArrivalDeadlineDate()` / `setArrivalDeadlineDate(Date)`
- `load()`
- `changeArrivalDeadline()`

**Load behavior**

1. Call `bookingServiceFacade.loadCargoForRouting(trackingId)`.
2. Store the returned `CargoRoute`.
3. Parse `cargo.getArrivalDeadlineDate()` using `MM/dd/yyyy`.
4. Store the resulting `Date` in `arrivalDeadlineDate`.
5. Do not query a repository or domain object directly.
6. Do not ignore a parsing failure or merely print its stack trace. Surface a
   clear application/view error consistent with existing JSF behavior.

**Submit behavior**

1. Refuse a null date through JSF validation or explicit bean validation.
2. Call
   `bookingServiceFacade.changeDeadline(trackingId, arrivalDeadlineDate)`.
3. Close the dynamic dialog with:

   ```java
   PrimeFaces.current().dialog().closeDynamic("DONE");
   ```

4. Do not close the dialog if the facade call fails.

**Tests to write**

Add a container-free JUnit test if practical, using a hand-written fake facade,
that proves:

- `load()` requests the correct tracking ID;
- `load()` converts an `MM/dd/yyyy` DTO date into the editable `Date`;
- `changeArrivalDeadline()` delegates the selected date and tracking ID;
- a malformed DTO deadline is surfaced rather than converted to null;
- a null selected date is rejected.

Do not add a mocking framework solely for these tests.

**Gating criteria**

- The bean is serializable and uses the established CDI/JSF annotations.
- The bean references only facade DTOs, not domain model classes.
- `./mvnw clean package -Popenliberty` succeeds on JDK 17.

### 4.4 — Issue 4: Implement the PrimeFaces deadline dialog

**What to build**

Add the session-scoped dialog launcher and the dynamic dialog view. The dialog
must work when addressed directly with a `trackingId` query parameter, but it
is not yet linked from the dashboard in this issue.

**Files to create**

- `src/main/java/org/eclipse/cargotracker/interfaces/booking/web/ChangeArrivalDeadlineDateDialog.java`
- `src/main/webapp/admin/dialogs/changeArrivalDeadlineDate.xhtml`

**Launcher requirements**

Use:

```java
@ManagedBean(name = "changeArrivalDeadlineDateDialog")
@SessionScoped
```

Implement:

- `showDialog(String trackingId)`
- `handleReturn(SelectEvent event)`
- `cancel()`

`showDialog(...)` must:

- set the options documented in Question 3.7;
- pass `trackingId` as a dynamic-dialog request parameter;
- open `/admin/dialogs/changeArrivalDeadlineDate.xhtml`.

`cancel()` must close the dialog without invoking the facade.

**XHTML requirements**

The page title must be:

```xhtml
<title>Change Deadline</title>
```

Place metadata directly beneath the root `<html>` element and before
`<h:head>`:

```xhtml
<f:metadata>
    <f:viewParam name="trackingId"
                 value="#{changeArrivalDeadlineDate.trackingId}"/>
    <f:viewAction action="#{changeArrivalDeadlineDate.load}"/>
</f:metadata>
```

The form must display:

- `Origin:` and `changeArrivalDeadlineDate.cargo.originName`;
- `Destination:` and
  `changeArrivalDeadlineDate.cargo.finalDestinationName`;
- `Deadline:` and a `p:datePicker` bound to
  `changeArrivalDeadlineDate.arrivalDeadlineDate`;
- **Cancel**, invoking
  `changeArrivalDeadlineDateDialog.cancel()`;
- **Update**, invoking
  `changeArrivalDeadlineDate.changeArrivalDeadline()`.

The date picker must require a value. The Update action must reload or refresh
the calling Administration view after a successful dialog close, following the
existing destination-dialog behavior.

**Runtime tests**

With the application running, request:

```text
http://localhost:8080/cargo-tracker/admin/dialogs/changeArrivalDeadlineDate.xhtml?trackingId=DEF789
```

Verify:

- HTTP 200;
- title is **Change Deadline**;
- origin and destination render;
- the existing deadline is selected;
- no `TagException`, `Parent UIComponent`, `FacesException`, or server error is
  present;
- Cancel does not change the persisted deadline;
- Update changes the deadline.

**Gating criteria**

- `./mvnw clean package -Popenliberty liberty:run` succeeds on JDK 17.
- Direct dialog loading and both actions work.
- Destination editing continues to work.
- Stop Liberty cleanly before completing the issue.

### 4.5 — Issue 5: Integrate deadline editing into the Administration dashboard

**What to build**

Replace the plain deadline text in the Not Routed Cargo table with the
PrimeFaces command-link affordance that opens the completed dialog and refreshes
the table after return.

**File to modify**

- `src/main/webapp/admin/tables/listNotRouted.xhtml`

**Required UI shape**

Within the existing Deadline column, add a `p:commandLink` that:

- calls
  `changeArrivalDeadlineDateDialog.showDialog(cargoNotRouted.trackingId)`;
- retains the displayed
  `cargoNotRouted.arrivalDeadlineDate`;
- adds the existing Font Awesome edit icon style;
- uses a stable component ID such as `arrivalDeadlineToUpdate`;
- listens for `dialogReturn`;
- invokes
  `changeArrivalDeadlineDateDialog.handleReturn`;
- updates `tableNotRouted`;
- provides the tooltip:
  `Click to change cargo arrival deadline date.`

Follow the adjacent Destination column's established structure and styling. Do
not alter tracking-ID routing or destination editing.

**End-to-end acceptance test**

1. Start from a clean build on JDK 17:

   ```bash
   ./mvnw clean package -Popenliberty liberty:run
   ```

2. Confirm the home page returns HTTP 200.
3. Open Administration and locate `DEF789`.
4. Record the original deadline.
5. Confirm the deadline now has an edit icon and tooltip.
6. Open the deadline dialog.
7. Confirm origin and destination identify the same cargo.
8. Choose a visibly different date.
9. Press **Update**.
10. Confirm the dialog closes and the Not Routed Cargo table refreshes.
11. Confirm the table shows the selected date.
12. Reload the browser and confirm the selected date remains.
13. Reopen the dialog and confirm the editor initializes to the changed date.
14. Press **Cancel** and confirm no additional change occurs.
15. Verify the Destination edit dialog still opens.
16. Verify selecting `DEF789` for routing still loads without an error page.

**Log acceptance**

The final run must contain none of:

- `<f:metadata> Parent UIComponent`;
- `TagException`;
- `VerifyError`;
- `FacesException`;
- `CWWKZ0002E` or `CWWKZ0003E`;
- recurring batch authorization failures;
- new FFDC files attributable to this feature.

Transient JMS activation-order warnings are acceptable only if all message
endpoints subsequently activate, as established by the prepared baseline.

**Final regression and scope checks**

- `./mvnw clean package -Popenliberty` succeeds.
- The existing test sources and the new deadline test compile.
- No Java EE namespace migration occurred.
- No Open Liberty, Derby, Jackson, JSF metadata, batch authorization, or REST
  compatibility fix from the starting commit was reverted.
- The feature affects only the intended application, facade, web, dialog,
  table, and test surfaces.
- Stop Liberty cleanly.

---
<shellId: 7 completed with exit code 0>
````

</details>


---

<sub>26s</sub>

### `bash`

**List all plan headings**

$ git show 'experiment/shepherd-control:1-arrival-deadline-control-remove-before-merge/add-change-arrival-deadline-feature-ignorance-reduction-plan.md' | awk '/^#{2,4} /{print}'

<details>
<summary>26 lines</summary>

```
## Goal
### User-visible acceptance behavior
### Domain acceptance behavior
### Hard scope constraints
## Completed phases
### Phase 1 ✅ — Establish a runnable feature-absent baseline
### Phase 2 ✅ — Verify the before and after user experience
## Phase 3 — Ignorance reduction: questions to answer before writing code
### 3.1 — Which cargos expose the edit operation?
### 3.2 — What is the exact domain mutation?
### 3.3 — What should happen to an existing itinerary and delivery state?
### 3.4 — What type crosses the facade boundary?
### 3.5 — How is the DTO's formatted deadline converted for editing?
### 3.6 — Which JSF bean scopes and interaction pattern should be used?
### 3.7 — What is the dynamic-dialog contract?
### 3.8 — What date validation is required?
### 3.9 — How will the feature be tested on the prepared historical baseline?
## Phase 4 — Implementation (five serial issues)
### 4.1 — Issue 1: Add the application-layer deadline change operation
### 4.2 — Issue 2: Expose deadline changes through the booking facade
### 4.3 — Issue 3: Implement the deadline editor backing model
### 4.4 — Issue 4: Implement the PrimeFaces deadline dialog
### 4.5 — Issue 5: Integrate deadline editing into the Administration dashboard
## Phase 5 — Documentation and implementation handoff
## Cross-cutting concerns
<shellId: 8 completed with exit code 0>
```

</details>


---

<sub>26s</sub>

### `bash`

**List campaign resources**

$ set -e
BRANCH='experiment/shepherd-control'
DIR='1-arrival-deadline-control-remove-before-merge'
git ls-tree -r --name-only "$BRANCH" "$DIR" | sed -n '1,240p'

```
1-arrival-deadline-control-remove-before-merge/add-change-arrival-deadline-feature-ignorance-reduction-plan.md
1-arrival-deadline-control-remove-before-merge/campaign-lessons.md
1-arrival-deadline-control-remove-before-merge/shepherd-campaign.json
1-arrival-deadline-control-remove-before-merge/shepherd-test-experiment.json
<shellId: 4 completed with exit code 0>
```


---

<sub>26s</sub>

### `bash`

**Verify campaign manifest**

$ git show 'experiment/shepherd-control:1-arrival-deadline-control-remove-before-merge/shepherd-campaign.json' | jq -e '.campaignId == "e87e808f-d031-4520-a1e5-6330b5749371" and .lessonPropagation == "off"' >/dev/null && printf 'campaign-manifest-matches\n'

```
campaign-manifest-matches
<shellId: 6 completed with exit code 0>
```


---

<sub>30s</sub>

### `view`

**view the file at /Users/edburns/.copilot/plugins/shepherd-task/scripts/validate-stage20-drafts.sh.**

/Users/edburns/.copilot/plugins/shepherd-task/scripts/validate-stage20-drafts.sh

<details>
<summary>65 lines</summary>

```
#!/usr/bin/env bash
# shepherd-task-version: 1.0.5

set -euo pipefail

[[ $# -eq 3 ]] || {
    echo "Usage: $0 <body-directory> <expected-count> <off|campaign>" >&2
    exit 1
}

body_directory="$1"
expected_count="$2"
lesson_propagation="$3"

[[ -d "$body_directory" ]] || {
    echo "Stage-20 body directory does not exist: $body_directory" >&2
    exit 1
}
[[ "$expected_count" =~ ^[1-9][0-9]*$ ]] || {
    echo "Expected count must be a positive integer." >&2
    exit 1
}
[[ "$lesson_propagation" == "off" || "$lesson_propagation" == "campaign" ]] || {
    echo "Lesson propagation must be off or campaign." >&2
    exit 1
}

body_files=()
for body_file in "$body_directory"/*-body.md; do
    [[ -f "$body_file" ]] || continue
    [[ "$body_file" != *-observed-body.md ]] || continue
    body_files+=("$body_file")
done
[[ ${#body_files[@]} -eq $expected_count ]] || {
    echo "Expected $expected_count persisted stage-20 body files; found ${#body_files[@]}." >&2
    exit 1
}

required_headings=(
    '## Campaign context and required reading'
    '## Branch and execution order'
    '## Implement'
    '## Completion gates'
    '## Out of scope'
)

for body_file in "${body_files[@]}"; do
    [[ -s "$body_file" && "$(awk 'END { print NR }' "$body_file")" -gt 1 ]] || {
        echo "Persisted issue body must contain physical Markdown lines: $body_file" >&2
        exit 1
    }
    first_nonblank="$(awk 'NF { print; exit }' "$body_file")"
    [[ "$first_nonblank" =~ ^##[[:space:]]+[^[:space:]] ]] || {
        echo "Persisted issue body must begin with a level-two heading: $body_file" >&2
        exit 1
    }
    for heading in "${required_headings[@]}"; do
        grep -Fqx -- "$heading" "$body_file" || {
            echo "Persisted issue body is missing the physical heading '$heading': $body_file" >&2
            exit 1
        }
    done
    if [[ "$lesson_propagation" == "campaign" ]]; then
        grep -Fqx -- '## Campaign lessons (REQUIRED)' "$body_file" || {
            echo "Treatment issue body is missing its campaign-lessons heading: $body_file" >&2
            exit 1
        }
    elif grep -Fqx -- '## Campaign lessons (REQUIRED)' "$body_file"; then
        echo "Control issue body unexpectedly contains a campaign-lessons heading: $body_file" >&2
        exit 1
    fi
done
```

</details>


---

<sub>31s</sub>

### Copilot

All nine implementation questions are concretely resolved, and the plan contains exactly five direct implementation tasks. I’m persisting the full, ordered issue bodies now; lesson propagation is off, so no campaign-lesson instructions will be included.

---

<sub>1m 23s</sub>

### `apply_patch`

<details>
<summary>Arguments</summary>

````json
"*** Begin Patch\n*** Add File: 1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339/issue-bodies/01-4-1-body.md\n+## Campaign context and required reading\n+\n+**On the `experiment/shepherd-control` branch, the directory `1-arrival-deadline-control-remove-before-merge` contains the plan (`add-change-arrival-deadline-feature-ignorance-reduction-plan.md`) and supporting resources (diagrams, decision records). Spike subdirectories are research artifacts — read the plan's Resolution sections for findings, not the spike source code.**\n+\n+Read the entire plan before working. Then re-read these exact sections:\n+\n+- `## Goal`\n+- `### Domain acceptance behavior`\n+- `### Hard scope constraints`\n+- `### 3.2 — What is the exact domain mutation?`\n+- `### 3.3 — What should happen to an existing itinerary and delivery state?`\n+- `### 3.8 — What date validation is required?`\n+- `### 3.9 — How will the feature be tested on the prepared historical baseline?`\n+- `### 4.1 — Issue 1: Add the application-layer deadline change operation`\n+- `## Cross-cutting concerns`\n+\n+The binding resolutions are:\n+\n+- Replace the `RouteSpecification` through the aggregate. Load the cargo, preserve its origin and destination, construct the replacement specification with the supplied deadline, call `cargo.specifyNewRoute(...)`, and persist through `cargoRepository.store(...)`. Do not add mutable deadline setters.\n+- Preserve the existing itinerary. Let `Cargo.specifyNewRoute(...)` recalculate delivery and routing state. In the established sequential test, the cargo remains `MISROUTED` after the deadline change.\n+- Add no new chronological business rule. The operation accepts the supplied non-null `Date` and relies on existing `RouteSpecification` invariants.\n+- Extend the sequential Arquillian specification, but do not modernize its historical remote-Payara runtime. The prepared JDK 17/Open Liberty build compiles tests while retaining `skipTests=true`.\n+\n+Research on the prepared historical baseline established that `./mvnw clean package -Popenliberty` compiles the test sources but does not execute the remote-Payara Arquillian suite. Treat compilation plus later Open Liberty HTTP/UI acceptance as the executable path; do not manufacture a passing test result by deleting, disabling, or rewriting the Arquillian configuration.\n+\n+## Branch and execution order\n+\n+Use `experiment/shepherd-control` from remote `origin` as the base branch. This is implementation subsection 4.1 and the first of five serial tasks. Tasks are assigned, completed, and merged to the base branch in plan order. Do not begin until this issue is assigned. Complete and merge this task before task 4.2 starts.\n+\n+## Implement\n+\n+Add the application-layer deadline-change use case with no web-layer changes.\n+\n+Modify:\n+\n+- `src/main/java/org/eclipse/cargotracker/application/BookingService.java`\n+- `src/main/java/org/eclipse/cargotracker/application/internal/DefaultBookingService.java`\n+- `src/test/java/org/eclipse/cargotracker/application/BookingServiceTest.java`\n+\n+Add this API:\n+\n+```java\n+void changeDeadline(TrackingId trackingId, Date deadline);\n+```\n+\n+Implement it by:\n+\n+1. Loading the cargo with `cargoRepository.find(trackingId)`.\n+2. Reading the current destination from `cargo.getRouteSpecification().getDestination()`.\n+3. Constructing a new `RouteSpecification` from `cargo.getOrigin()`, the current destination, and the supplied deadline.\n+4. Calling `cargo.specifyNewRoute(routeSpecification)`.\n+5. Calling `cargoRepository.store(cargo)`.\n+6. Logging the tracking ID and new deadline at `Level.INFO` in the style of `changeDestination(...)`.\n+\n+Write the test first. Append sequential `testChangeDeadline()` after `testChangeDestination()`. Build a deadline one month after the test's original `deadline`, invoke the service, reload with `Cargo.findByTrackingId`, and assert:\n+\n+- origin remains Chicago;\n+- destination remains Helsinki;\n+- the stored deadline is the same calendar day as the requested deadline;\n+- the assigned itinerary is unchanged;\n+- transport status remains `NOT_RECEIVED`;\n+- last known location remains `Location.UNKNOWN`;\n+- current voyage remains `Voyage.NONE`;\n+- the cargo is not misdirected;\n+- estimated time of arrival is `Delivery.ETA_UNKOWN`;\n+- next expected activity is `Delivery.NO_ACTIVITY`;\n+- the cargo is not unloaded at destination;\n+- routing status remains `MISROUTED`.\n+\n+As an additional regression gate, assert before and after values for the itinerary object/legs and destination so an implementation that silently reroutes, clears the itinerary, or changes destination cannot pass.\n+\n+## Completion gates\n+\n+- `./mvnw clean package -Popenliberty` succeeds on JDK 17.\n+- The added Arquillian test source compiles in the package build and remains immediately after `testChangeDestination()` in the intended sequence.\n+- The implementation uses the aggregate operation and repository exactly once each.\n+- The complete preserved/recalculated state listed above is asserted.\n+- The diff contains no JSF, PrimeFaces, facade, REST, Liberty, or persistence-configuration changes.\n+\n+## Out of scope\n+\n+- Any JSF, PrimeFaces, facade, REST, or dashboard work.\n+- A setter on `Cargo`, `RouteSpecification`, or another domain object.\n+- Clearing, replacing, or rerouting the itinerary.\n+- Direct persistence-entity updates.\n+- New future-date, old-deadline, or itinerary-leg chronology rules.\n+- Arquillian/Payara modernization, new mocking libraries, namespace migration, or build-configuration changes.\n*** Add File: 1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339/issue-bodies/02-4-2-body.md\n+## Campaign context and required reading\n+\n+**On the `experiment/shepherd-control` branch, the directory `1-arrival-deadline-control-remove-before-merge` contains the plan (`add-change-arrival-deadline-feature-ignorance-reduction-plan.md`) and supporting resources (diagrams, decision records). Spike subdirectories are research artifacts — read the plan's Resolution sections for findings, not the spike source code.**\n+\n+Read the entire plan before working. Then re-read these exact sections:\n+\n+- `## Goal`\n+- `### Hard scope constraints`\n+- `### 3.2 — What is the exact domain mutation?`\n+- `### 3.4 — What type crosses the facade boundary?`\n+- `### 3.9 — How will the feature be tested on the prepared historical baseline?`\n+- `### 4.2 — Issue 2: Expose deadline changes through the booking facade`\n+- `## Cross-cutting concerns`\n+\n+The binding resolutions are:\n+\n+- The application operation already performs the aggregate mutation by replacing the route specification; the facade must only delegate.\n+- Add `void changeDeadline(String trackingId, Date arrivalDeadline)` to `BookingServiceFacade`.\n+- `DefaultBookingServiceFacade` converts the identifier with `new TrackingId(trackingId)` and passes the same `Date` to `BookingService.changeDeadline(...)`.\n+- Do not introduce a command DTO, formatted-string deadline parameter, domain type in the JSF boundary, repository access, or duplicate mutation logic.\n+- The prepared JDK 17/Open Liberty package is the mandatory executable build gate; do not expand this issue into historical Arquillian-runtime modernization.\n+\n+Research established that the historical baseline consistently uses `String` identifiers and `java.util.Date` at this facade boundary. Keeping conversion limited to `TrackingId` preserves that contract and avoids format parsing or domain leakage in presentation clients.\n+\n+## Branch and execution order\n+\n+Use `experiment/shepherd-control` from remote `origin` as the base branch. This is implementation subsection 4.2 and the second of five serial tasks. Tasks are assigned, completed, and merged to the base branch in plan order. Do not begin until this issue is assigned and task 4.1 is merged. Complete and merge this task before task 4.3 starts.\n+\n+## Implement\n+\n+Modify:\n+\n+- `src/main/java/org/eclipse/cargotracker/interfaces/booking/facade/BookingServiceFacade.java`\n+- `src/main/java/org/eclipse/cargotracker/interfaces/booking/facade/internal/DefaultBookingServiceFacade.java`\n+\n+Optionally add:\n+\n+- `src/test/java/org/eclipse/cargotracker/interfaces/booking/facade/internal/DefaultBookingServiceFacadeTest.java`\n+\n+Add:\n+\n+```java\n+void changeDeadline(String trackingId, Date arrivalDeadline);\n+```\n+\n+Delegate with the equivalent of:\n+\n+```java\n+bookingService.changeDeadline(\n+        new TrackingId(trackingId),\n+        arrivalDeadline);\n+```\n+\n+If a focused container-free test fits the existing test construction pattern, use a hand-written `BookingService` fake/spy. Prove that the tracking string becomes an equivalent `TrackingId`, the exact selected `Date` object/value reaches the application service, and delegation occurs exactly once. The fake should fail if any unrelated application-service method is called.\n+\n+As an additional contract gate, compile an existing facade consumer and verify no existing method signature changes. Ensure the facade does not load a cargo, access a repository, parse/format dates, or reconstruct a route specification.\n+\n+## Completion gates\n+\n+- `./mvnw clean package -Popenliberty` succeeds on JDK 17.\n+- Existing facade consumers compile unchanged.\n+- The application-layer API and test from task 4.1 remain unchanged and compile.\n+- The facade delegates exactly once and performs only tracking-ID conversion.\n+- If added, the focused test uses a hand-written fake and no new dependency.\n+- The diff contains only facade API/implementation changes and an optional focused facade test.\n+\n+## Out of scope\n+\n+- Loading or mutating `Cargo` in the facade.\n+- Calling `CargoRepository` from the facade.\n+- Parsing a formatted date or introducing a request DTO.\n+- JSF, PrimeFaces, backing-bean, dialog, and dashboard changes.\n+- Mockito or another new test dependency.\n+- Arquillian/Payara modernization, Java EE namespace migration, or build-configuration changes.\n*** Add File: 1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339/issue-bodies/03-4-3-body.md\n+## Campaign context and required reading\n+\n+**On the `experiment/shepherd-control` branch, the directory `1-arrival-deadline-control-remove-before-merge` contains the plan (`add-change-arrival-deadline-feature-ignorance-reduction-plan.md`) and supporting resources (diagrams, decision records). Spike subdirectories are research artifacts — read the plan's Resolution sections for findings, not the spike source code.**\n+\n+Read the entire plan before working. Then re-read these exact sections:\n+\n+- `## Goal`\n+- `### User-visible acceptance behavior`\n+- `### Hard scope constraints`\n+- `### 3.4 — What type crosses the facade boundary?`\n+- `### 3.5 — How is the DTO's formatted deadline converted for editing?`\n+- `### 3.6 — Which JSF bean scopes and interaction pattern should be used?`\n+- `### 3.7 — What is the dynamic-dialog contract?`\n+- `### 3.8 — What date validation is required?`\n+- `### 3.9 — How will the feature be tested on the prepared historical baseline?`\n+- `### 4.3 — Issue 3: Implement the deadline editor backing model`\n+- `## Cross-cutting concerns`\n+\n+The binding resolutions are:\n+\n+- The facade boundary uses `String` tracking IDs and `java.util.Date` deadlines; do not expose domain objects to this bean.\n+- Keep conversion inside the view-scoped editor. Load `CargoRoute`, create a per-load `SimpleDateFormat(\"MM/dd/yyyy\")`, and parse the date portion exposed by the DTO. Do not add a shared mutable formatter.\n+- Implement a serializable CDI `@Named @ViewScoped` bean named `changeArrivalDeadlineDate`.\n+- Successful submission delegates through the facade and closes the dynamic dialog with `\"DONE\"`.\n+- Require a non-null selected date but add no minimum-date, future-date, previous-deadline, or itinerary-date rule.\n+\n+Research established that the DTO's formatted arrival deadline begins with `MM/dd/yyyy`, matching the date displayed by `getArrivalDeadlineDate()`. Parse the DTO's date representation per load and surface malformed data explicitly; silently converting a parse failure to `null` would turn corrupt state into a misleading validation error.\n+\n+## Branch and execution order\n+\n+Use `experiment/shepherd-control` from remote `origin` as the base branch. This is implementation subsection 4.3 and the third of five serial tasks. Tasks are assigned, completed, and merged to the base branch in plan order. Do not begin until this issue is assigned and task 4.2 is merged. Complete and merge this task before task 4.4 starts.\n+\n+## Implement\n+\n+Create:\n+\n+- `src/main/java/org/eclipse/cargotracker/interfaces/booking/web/ChangeArrivalDeadlineDate.java`\n+\n+Use this shape:\n+\n+```java\n+@Named\n+@ViewScoped\n+public class ChangeArrivalDeadlineDate implements Serializable {\n+    private static final long serialVersionUID = 1L;\n+\n+    private String trackingId;\n+    private CargoRoute cargo;\n+    private Date arrivalDeadlineDate;\n+\n+    @Inject\n+    private BookingServiceFacade bookingServiceFacade;\n+}\n+```\n+\n+Provide:\n+\n+- `getTrackingId()` and `setTrackingId(String)`;\n+- `getCargo()`;\n+- `getArrivalDeadlineDate()` and `setArrivalDeadlineDate(Date)`;\n+- `load()`;\n+- `changeArrivalDeadline()`.\n+\n+`load()` must call `bookingServiceFacade.loadCargoForRouting(trackingId)`, retain the returned `CargoRoute`, parse its `MM/dd/yyyy` deadline representation with a newly created formatter, and retain the resulting `Date`. Do not access a repository or domain model. Handle parse failure through an explicit application/view error consistent with neighboring beans; do not swallow it or only print a stack trace.\n+\n+`changeArrivalDeadline()` must reject a null selected date, invoke `bookingServiceFacade.changeDeadline(trackingId, arrivalDeadlineDate)`, and only after successful return call:\n+\n+```java\n+PrimeFaces.current().dialog().closeDynamic(\"DONE\");\n+```\n+\n+Add a container-free JUnit test when practical using a hand-written fake facade. Cover correct tracking-ID load, `MM/dd/yyyy` conversion, exact submit delegation, malformed DTO data, and null selection. As an additional gate, verify a facade failure propagates and does not reach the close call; if PrimeFaces static context makes this unsuitable for a unit test, preserve the control-flow ordering clearly and cover it in task 4.4 runtime acceptance.\n+\n+## Completion gates\n+\n+- `./mvnw clean package -Popenliberty` succeeds on JDK 17.\n+- The bean is serializable and uses the established CDI `@Named` and JSF `@ViewScoped` annotations.\n+- The bean depends only on the booking facade and facade DTOs, not domain or repository classes.\n+- Each load creates its own formatter.\n+- Malformed source data and null selected dates fail explicitly.\n+- The dialog closes only after successful facade delegation.\n+- No dialog launcher or XHTML is added.\n+\n+## Out of scope\n+\n+- Dialog launcher, XHTML, Administration-table integration, or navigation changes.\n+- Repository or domain-model access from the backing bean.\n+- Changes to `CargoRoute` solely to add a `Date` field.\n+- A shared/static `SimpleDateFormat`.\n+- New chronological business validation.\n+- New mocking dependencies or historical test-runtime modernization.\n*** Add File: 1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339/issue-bodies/04-4-4-body.md\n+## Campaign context and required reading\n+\n+**On the `experiment/shepherd-control` branch, the directory `1-arrival-deadline-control-remove-before-merge` contains the plan (`add-change-arrival-deadline-feature-ignorance-reduction-plan.md`) and supporting resources (diagrams, decision records). Spike subdirectories are research artifacts — read the plan's Resolution sections for findings, not the spike source code.**\n+\n+Read the entire plan before working. Then re-read these exact sections:\n+\n+- `## Goal`\n+- `### User-visible acceptance behavior`\n+- `### Hard scope constraints`\n+- `### 3.1 — Which cargos expose the edit operation?`\n+- `### 3.5 — How is the DTO's formatted deadline converted for editing?`\n+- `### 3.6 — Which JSF bean scopes and interaction pattern should be used?`\n+- `### 3.7 — What is the dynamic-dialog contract?`\n+- `### 3.8 — What date validation is required?`\n+- `### 3.9 — How will the feature be tested on the prepared historical baseline?`\n+- `### 4.4 — Issue 4: Implement the PrimeFaces deadline dialog`\n+- `## Cross-cutting concerns`\n+\n+The binding resolutions are:\n+\n+- Mirror the existing Change Destination interaction with a serializable JSF `@ManagedBean(name = \"changeArrivalDeadlineDateDialog\") @SessionScoped` launcher and the view-scoped editor completed in task 4.3.\n+- Open `/admin/dialogs/changeArrivalDeadlineDate.xhtml` with one `trackingId` parameter. Set `modal=true`, `draggable=true`, `resizable=false`, `contentWidth=410`, and `contentHeight=280`.\n+- Successful update closes with `\"DONE\"` and cancellation with the empty string.\n+- Place `<f:metadata>` directly beneath the root `<html>` and before `<h:head>` and `<h:body>`.\n+- Require a date value but add no chronological rule.\n+- The dashboard affordance is limited to the Not Routed table, but that integration belongs to task 4.5.\n+\n+Research on the prepared Open Liberty/MyFaces baseline established that placing `<f:metadata>` inside `<h:body>` violates the `UIViewRoot` requirement and can produce `TagException`/parent-component failures. Preserve the direct-root placement exactly. The established destination dialog provides the interaction pattern, but implement this production dialog independently rather than copying any research artifact.\n+\n+## Branch and execution order\n+\n+Use `experiment/shepherd-control` from remote `origin` as the base branch. This is implementation subsection 4.4 and the fourth of five serial tasks. Tasks are assigned, completed, and merged to the base branch in plan order. Do not begin until this issue is assigned and task 4.3 is merged. Complete and merge this task before task 4.5 starts.\n+\n+## Implement\n+\n+Create:\n+\n+- `src/main/java/org/eclipse/cargotracker/interfaces/booking/web/ChangeArrivalDeadlineDateDialog.java`\n+- `src/main/webapp/admin/dialogs/changeArrivalDeadlineDate.xhtml`\n+\n+The launcher must use:\n+\n+```java\n+@ManagedBean(name = \"changeArrivalDeadlineDateDialog\")\n+@SessionScoped\n+```\n+\n+Implement `showDialog(String trackingId)`, `handleReturn(SelectEvent event)`, and `cancel()`. `showDialog(...)` must provide the exact options and single request parameter above, then open `/admin/dialogs/changeArrivalDeadlineDate.xhtml`. `cancel()` must close with the empty string and must not invoke the facade.\n+\n+The XHTML title is `Change Deadline`. Put this metadata directly under `<html>` before `<h:head>`:\n+\n+```xhtml\n+<f:metadata>\n+    <f:viewParam name=\"trackingId\"\n+                 value=\"#{changeArrivalDeadlineDate.trackingId}\"/>\n+    <f:viewAction action=\"#{changeArrivalDeadlineDate.load}\"/>\n+</f:metadata>\n+```\n+\n+Display origin, destination, and deadline. Bind origin to `changeArrivalDeadlineDate.cargo.originName`, destination to `changeArrivalDeadlineDate.cargo.finalDestinationName`, and a required `p:datePicker` to `changeArrivalDeadlineDate.arrivalDeadlineDate`. Add Cancel calling `changeArrivalDeadlineDateDialog.cancel()` and Update calling `changeArrivalDeadlineDate.changeArrivalDeadline()`. Follow the destination dialog's return lifecycle so the eventual caller can refresh after successful close.\n+\n+Run the application and directly request:\n+\n+`http://localhost:8080/cargo-tracker/admin/dialogs/changeArrivalDeadlineDate.xhtml?trackingId=DEF789`\n+\n+Verify HTTP 200, the exact title, rendered origin/destination, initialized deadline, successful update, and cancellation with no persistence change. Also verify destination editing still works. Inspect runtime output for `TagException`, `Parent UIComponent`, `FacesException`, and server errors.\n+\n+As an additional gate, directly request the view once without `trackingId` and confirm it fails explicitly or renders the repository-standard validation/error behavior rather than mutating an arbitrary cargo.\n+\n+## Completion gates\n+\n+- `./mvnw clean package -Popenliberty liberty:run` succeeds on JDK 17.\n+- Direct dialog loading for `DEF789` returns HTTP 200 and displays the expected cargo and date.\n+- Update persists a changed deadline; Cancel persists nothing.\n+- Metadata is a direct child of `<html>` before head/body.\n+- No `TagException`, `Parent UIComponent`, `FacesException`, or feature-attributable server error appears.\n+- Existing destination editing still works.\n+- Liberty is stopped cleanly before completion.\n+- No dashboard table file changes in this issue.\n+\n+## Out of scope\n+\n+- Adding the deadline link to the Administration dashboard.\n+- Inline cell editing or full-page navigation.\n+- Additional request parameters or different dialog dimensions.\n+- Chronological business rules.\n+- Direct repository/domain access from web classes.\n+- Changes to the existing destination-edit feature except what is strictly required to preserve compatibility.\n+- Runtime modernization, namespace migration, or unrelated Liberty configuration.\n*** Add File: 1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339/issue-bodies/05-4-5-body.md\n+## Campaign context and required reading\n+\n+**On the `experiment/shepherd-control` branch, the directory `1-arrival-deadline-control-remove-before-merge` contains the plan (`add-change-arrival-deadline-feature-ignorance-reduction-plan.md`) and supporting resources (diagrams, decision records). Spike subdirectories are research artifacts — read the plan's Resolution sections for findings, not the spike source code.**\n+\n+Read the entire plan before working. Then re-read these exact sections:\n+\n+- `## Goal`\n+- `### User-visible acceptance behavior`\n+- `### Domain acceptance behavior`\n+- `### Hard scope constraints`\n+- `### Phase 1 ✅ — Establish a runnable feature-absent baseline`\n+- `### Phase 2 ✅ — Verify the before and after user experience`\n+- `### 3.1 — Which cargos expose the edit operation?`\n+- `### 3.3 — What should happen to an existing itinerary and delivery state?`\n+- `### 3.6 — Which JSF bean scopes and interaction pattern should be used?`\n+- `### 3.7 — What is the dynamic-dialog contract?`\n+- `### 3.8 — What date validation is required?`\n+- `### 3.9 — How will the feature be tested on the prepared historical baseline?`\n+- `### 4.5 — Issue 5: Integrate deadline editing into the Administration dashboard`\n+- `## Cross-cutting concerns`\n+\n+The binding resolutions are:\n+\n+- Expose deadline editing only in `src/main/webapp/admin/tables/listNotRouted.xhtml`; do not add it to routed, misrouted, claimed, or other tables.\n+- Preserve itinerary and allow aggregate recalculation; the application/facade operations remain generally callable by tracking ID even though this UI is scoped to Not Routed cargo.\n+- Use the established PrimeFaces dynamic-dialog lifecycle. The caller listens for `dialogReturn`, invokes the dialog launcher's return handler, and updates `tableNotRouted`.\n+- Require a non-null date, with no new chronological policy.\n+- Mandatory executable evidence is the JDK 17/Open Liberty build/start, direct HTTP checks, and complete `DEF789` browser flow. Preserve the historical Arquillian path without trying to modernize it.\n+\n+Research on the prepared baseline established that metadata placement in the dialog must remain directly under the root element and that transient JMS activation-order warnings are acceptable only when all endpoints subsequently activate. Recurring batch authorization failures, deployment errors, or new feature-attributable FFDC files are not acceptable.\n+\n+## Branch and execution order\n+\n+Use `experiment/shepherd-control` from remote `origin` as the base branch. This is implementation subsection 4.5 and the fifth and final serial task. Tasks are assigned, completed, and merged to the base branch in plan order. Do not begin until this issue is assigned and task 4.4 is merged.\n+\n+## Implement\n+\n+Modify only:\n+\n+- `src/main/webapp/admin/tables/listNotRouted.xhtml`\n+\n+Within the existing Deadline column, replace the plain deadline presentation with a `p:commandLink` that:\n+\n+- calls `changeArrivalDeadlineDateDialog.showDialog(cargoNotRouted.trackingId)`;\n+- continues displaying `cargoNotRouted.arrivalDeadlineDate`;\n+- uses the existing Font Awesome edit-icon style;\n+- has a stable component ID such as `arrivalDeadlineToUpdate`;\n+- listens for `dialogReturn`;\n+- invokes `changeArrivalDeadlineDateDialog.handleReturn`;\n+- updates `tableNotRouted`;\n+- has tooltip text exactly `Click to change cargo arrival deadline date.`\n+\n+Follow the adjacent Destination column's established command-link structure and styling. Do not alter tracking-ID routing or destination editing.\n+\n+Perform the complete `DEF789` acceptance flow:\n+\n+1. Run `./mvnw clean package -Popenliberty liberty:run` on JDK 17.\n+2. Confirm the home page returns HTTP 200.\n+3. Open Administration, locate `DEF789`, and record its original deadline.\n+4. Confirm the deadline has the edit icon and exact tooltip.\n+5. Open the deadline dialog and verify origin/destination identify the same cargo.\n+6. Select a visibly different date and press Update.\n+7. Confirm the dialog closes, `tableNotRouted` refreshes, and the selected date is displayed.\n+8. Reload and confirm the value persisted.\n+9. Reopen and confirm the editor initializes to the changed date.\n+10. Cancel and confirm no further change.\n+11. Verify Destination editing still opens.\n+12. Verify selecting `DEF789` for routing still loads without an error page.\n+\n+As additional regression gates, verify no deadline-edit affordance appears in the other Administration cargo tables, the command link preserves the original deadline text formatting, and the Ajax update targets an existing component without a component-not-found error.\n+\n+## Completion gates\n+\n+- `./mvnw clean package -Popenliberty` succeeds and all test sources, including the deadline test, compile.\n+- The full `DEF789` acceptance flow passes, including persistence after reload and unchanged state after Cancel.\n+- Destination editing and routing selection remain functional.\n+- Runtime logs contain none of: `<f:metadata> Parent UIComponent`, `TagException`, `VerifyError`, `FacesException`, `CWWKZ0002E`, `CWWKZ0003E`, recurring batch authorization failures, or new feature-attributable FFDC files.\n+- Any transient JMS activation-order warning is followed by successful activation of every endpoint.\n+- No Java EE namespace migration or prepared Liberty, Derby, Jackson, JSF metadata, batch authorization, or REST compatibility fix is reverted.\n+- The final diff is limited to the intended application, facade, web, dialog, table, and test surfaces accumulated by the five tasks.\n+- Liberty is stopped cleanly.\n+\n+## Out of scope\n+\n+- Deadline editing in any Administration table other than Not Routed Cargo.\n+- Changes to tracking-ID routing or destination editing.\n+- New application/facade/domain behavior beyond what tasks 4.1–4.4 already delivered.\n+- Inline editing, full-page navigation, new chronological rules, or visual redesign.\n+- Java EE namespace migration, infrastructure/runtime modernization, or unrelated baseline fixes.\n*** End Patch\n"
````

</details>

```
Added 5 file(s): /Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339/issue-bodies/01-4-1-body.md, /Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339/issue-bodies/02-4-2-body.md, /Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339/issue-bodies/03-4-3-body.md, /Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339/issue-bodies/04-4-4-body.md, /Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339/issue-bodies/05-4-5-body.md
```


---

<sub>1m 28s</sub>

### `view`

**view the file at /Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-github-issue-body.sh.**

/Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-github-issue-body.sh

<details>
<summary>131 lines</summary>

```
#!/usr/bin/env bash
# shepherd-task-version: 1.0.5
set -euo pipefail

fail() {
    printf 'verify-github-issue-body: %s\n' "$*" >&2
    exit 1
}

[[ $# -ge 3 && $# -le 6 ]] ||
    fail "usage: $0 OWNER/REPO ISSUE_NUMBER EXPECTED_BODY_PATH [MAX_ATTEMPTS] [DELAY_SECONDS] [DIAGNOSTIC_PATH]"

repository="$1"
issue_number="$2"
expected_body_path="$3"
max_attempts="${4:-6}"
delay_seconds="${5:-5}"
diagnostic_path="${6:-}"
gh_command="${GH_COMMAND:-gh}"

[[ "$repository" =~ ^[A-Za-z0-9_.-]+/[A-Za-z0-9_.-]+$ ]] ||
    fail "invalid repository: $repository"
[[ "$issue_number" =~ ^[1-9][0-9]*$ ]] ||
    fail "invalid issue number: $issue_number"
[[ "$max_attempts" =~ ^[1-9][0-9]*$ ]] ||
    fail "MAX_ATTEMPTS must be a positive integer"
[[ "$delay_seconds" =~ ^[0-9]+$ ]] ||
    fail "DELAY_SECONDS must be a non-negative integer"
[[ -f "$expected_body_path" ]] ||
    fail "expected issue body file not found: $expected_body_path"

temp_directory="$(mktemp -d)"
trap 'rm -rf "$temp_directory"' EXIT
response_path="$temp_directory/response.json"
actual_path="$temp_directory/actual.txt"
actual_normalized="$temp_directory/actual-normalized.txt"
expected_normalized="$temp_directory/expected-normalized.txt"

normalize_file() {
    jq -b -Rsj 'gsub("\r\n|\r"; "\n")' "$1" >"$2"
}

equivalent_files() {
    local actual="$1"
    local expected="$2"
    local candidate="$temp_directory/candidate.txt"

    cmp -s -- "$actual" "$expected" && return 0
    cp "$actual" "$candidate"
    printf '\n' >>"$candidate"
    cmp -s -- "$candidate" "$expected" && return 0
    cp "$expected" "$candidate"
    printf '\n' >>"$candidate"
    cmp -s -- "$actual" "$candidate"
}

sha256_file() {
    if command -v sha256sum >/dev/null 2>&1; then
        sha256sum "$1" | awk '{print $1}'
    else
        shasum -a 256 "$1" | awk '{print $1}'
    fi
}

write_diagnostic() {
    local reason="$1"
    local attempts="$2"
    [[ -n "$diagnostic_path" ]] || return 0

    mkdir -p "$(dirname "$diagnostic_path")"
    local expected_length actual_length expected_hash actual_hash first_offset
    expected_length="$(wc -c <"$expected_normalized" | tr -d ' ')"
    actual_length="$(wc -c <"$actual_normalized" | tr -d ' ')"
    expected_hash="$(sha256_file "$expected_normalized")"
    actual_hash="$(sha256_file "$actual_normalized")"
    first_offset="$( (cmp -l -- "$actual_normalized" "$expected_normalized" 2>/dev/null || true) | awk 'NR == 1 { print $1 - 1 }')"
    [[ -n "$first_offset" ]] || first_offset="null"

    jq -n \
        --arg repository "$repository" \
        --argjson issueNumber "$issue_number" \
        --arg endpoint "repos/$repository/issues/$issue_number" \
        --argjson attempts "$attempts" \
        --arg observedAt "$(date -u +%Y-%m-%dT%H:%M:%SZ)" \
        --arg reason "$reason" \
        --argjson expectedLength "$expected_length" \
        --argjson actualLength "$actual_length" \
        --arg expectedSha256 "$expected_hash" \
        --arg actualSha256 "$actual_hash" \
        --argjson firstDifferenceOffset "$first_offset" \
        '{
            schemaVersion: 1,
            repository: $repository,
            issueNumber: $issueNumber,
            endpoint: $endpoint,
            attempts: $attempts,
            observedAt: $observedAt,
            reason: $reason,
            expectedLength: $expectedLength,
            actualLength: $actualLength,
            expectedSha256: $expectedSha256,
            actualSha256: $actualSha256,
            firstDifferenceOffset: $firstDifferenceOffset
        }' >"$diagnostic_path"
}

normalize_file "$expected_body_path" "$expected_normalized"
last_reason=""

for ((attempt = 1; attempt <= max_attempts; attempt++)); do
    set +e
    "$gh_command" api "repos/$repository/issues/$issue_number" >"$response_path" 2>"$temp_directory/error.txt"
    exit_code=$?
    set -e

    if [[ $exit_code -ne 0 ]]; then
        last_reason="GitHub REST request failed with exit code $exit_code."
        error_message="$(cat "$temp_directory/error.txt")"
        authentication_error_pattern='HTTP[[:space:]]+(401|403)|[Aa]uthentication|[Nn][Oo][Tt][[:space:]]authorized|[Rr]esource[[:space:]]not[[:space:]]accessible'
        if [[ "$error_message" =~ $authentication_error_pattern ]]; then
            : >"$actual_normalized"
            write_diagnostic "$last_reason" "$attempt"
            fail "unable to fetch issue #$issue_number from $repository: $error_message"
        fi
    elif ! jq -e 'type == "object" and has("body")' "$response_path" >/dev/null 2>&1; then
        last_reason="GitHub REST response was invalid JSON."
    else
        jq -b -j '.body // ""' "$response_path" >"$actual_path"
        normalize_file "$actual_path" "$actual_normalized"
        if equivalent_files "$actual_normalized" "$expected_normalized"; then
            cat "$response_path"
            exit 0
        fi
        last_reason="GitHub issue body differs from the persisted draft."
    fi

    if [[ $attempt -lt $max_attempts ]]; then
        printf 'verify-github-issue-body: %s Retrying issue #%s body verification (%s/%s).\n' \
            "$last_reason" "$issue_number" "$attempt" "$max_attempts" >&2
        ((delay_seconds > 0)) && sleep "$delay_seconds"
    fi
done

[[ -f "$actual_normalized" ]] || : >"$actual_normalized"
write_diagnostic "$last_reason" "$max_attempts"
diagnostic_suffix=""
[[ -z "$diagnostic_path" ]] || diagnostic_suffix=" Diagnostic: $diagnostic_path"
fail "issue #$issue_number body verification failed after $max_attempts attempts. $last_reason$diagnostic_suffix"
```

</details>


---

<sub>1m 28s</sub>

### `view`

**view the file at /Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-stage20-child-links.sh.**

/Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-stage20-child-links.sh

<details>
<summary>30 lines</summary>

```
#!/usr/bin/env bash
# shepherd-task-version: 1.0.5

set -euo pipefail

if [[ $# -ne 3 ]]; then
    echo "Usage: $0 <PRE_CREATION_CHILDREN_JSON> <FINAL_CHILDREN_JSON> <CREATION_LEDGER_JSON>" >&2
    exit 2
fi

for command_name in jq; do
    command -v "$command_name" >/dev/null 2>&1 || {
        echo "Error: Required command '$command_name' was not found." >&2
        exit 1
    }
done

script_dir="$(cd "$(dirname "$0")" && pwd)"
filter_path="$script_dir/verify-stage20-child-links.jq"
[[ -f "$filter_path" ]] || {
    echo "Error: Stage-20 child-link verification filter was not found: $filter_path" >&2
    exit 1
}

for input_path in "$@"; do
    [[ -f "$input_path" ]] || {
        echo "Error: Stage-20 child-link verification input was not found: $input_path" >&2
        exit 1
    }
done

jq -n \
    --slurpfile baseline "$1" \
    --slurpfile final "$2" \
    --slurpfile ledger "$3" \
    -f "$filter_path"
```

</details>


---

<sub>1m 41s</sub>

### `bash`

**Validate drafts and initialize state**

$ set -euo pipefail
LOG_DIRECTORY='/Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339'
DRAFT_VALIDATOR='/Users/edburns/.copilot/plugins/shepherd-task/scripts/validate-stage20-drafts.sh'
REPO='edburns/dd-3070094-cargotracker-darwin-arm64-01'
LEDGER="$LOG_DIRECTORY/creation-ledger.json"
RESULT="$LOG_DIRECTORY/stage-20-result.json"
BASELINE="$LOG_DIRECTORY/pre-creation-children.json"
atomic_write() {
  local destination="$1"
  local content="$2"
  local temporary

  temporary="$(mktemp "${destination}.tmp.XXXXXX")" || return 1

  if ! printf '%s\n' "$content" > "$temporary"; then
    rm -f "$temporary"
    return 1
  fi

  if ! mv "$temporary" "$destination"; then
    rm -f "$temporary"
    return 1
  fi
}
"$DRAFT_VALIDATOR" \
  "$LOG_DIRECTORY/issue-bodies" \
  5 \
  off
atomic_write "$LEDGER" '[]'
atomic_write "$RESULT" \
  '{"schemaVersion":1,"status":"in_progress","ledgerFile":"creation-ledger.json","operationError":null}'
jq -e 'type == "array" and length == 0' "$LEDGER" >/dev/null
jq -e \
  '.schemaVersion == 1 and
   .status == "in_progress" and
   .ledgerFile == "creation-ledger.json" and
   .operationError == null' \
  "$RESULT" >/dev/null
children_pages="$(gh api "repos/$REPO/issues/1/sub_issues" --paginate --slurp)"
children_flat="$(printf '%s\n' "$children_pages" | jq 'if length == 0 then [] elif all(.[]; type == "array") then add else . end')"
atomic_write "$BASELINE" "$children_flat"
jq -e 'type == "array" and length == 0' "$BASELINE" >/dev/null
printf 'drafts-validated-and-state-initialized\n'

```
drafts-validated-and-state-initialized
<shellId: 9 completed with exit code 0>
```


---

<sub>1m 45s</sub>

### Copilot

All five persisted drafts passed the stage validator, and the empty ledger, in-progress result, and normalized pre-creation snapshot were atomically initialized and re-read successfully. GitHub mutation now proceeds strictly one issue at a time in plan order, with body verification before each link.

---

<sub>1m 53s</sub>

### `bash`

**Create and link first issue**

$ set -euo pipefail
REPO='edburns/dd-3070094-cargotracker-darwin-arm64-01'
PARENT_ISSUE=1
LOG_DIRECTORY='/Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339'
BODY_FILE="$LOG_DIRECTORY/issue-bodies/01-4-1-body.md"
BODY_RELATIVE='issue-bodies/01-4-1-body.md'
SUBSECTION='4.1 — Issue 1: Add the application-layer deadline change operation'
TITLE='4.1 — Add the application-layer deadline change operation'
LEDGER="$LOG_DIRECTORY/creation-ledger.json"
VERIFIER='/Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-github-issue-body.sh'
atomic_write() {
  local destination="$1"
  local content="$2"
  local temporary
  temporary="$(mktemp "${destination}.tmp.XXXXXX")" || return 1
  if ! printf '%s\n' "$content" > "$temporary"; then rm -f "$temporary"; return 1; fi
  if ! mv "$temporary" "$destination"; then rm -f "$temporary"; return 1; fi
}
update_ledger_flag() {
  local number="$1" field="$2" value="$3" updated
  updated="$(
    jq \
      --argjson number "$number" \
      --arg field "$field" \
      --argjson value "$value" \
      'map(if .number == $number then .[$field] = $value else . end)' \
      "$LEDGER"
  )" || return 1
  atomic_write "$LEDGER" "$updated"
}
created="$(gh api "repos/$REPO/issues" -X POST -f title="$TITLE" -F "body=@$BODY_FILE" --jq '{id,number,node_id,html_url,title}')"
id="$(jq -r '.id' <<<"$created")"; number="$(jq -r '.number' <<<"$created")"; url="$(jq -r '.html_url' <<<"$created")"
updated="$(jq --arg subsection "$SUBSECTION" --arg body "$BODY_RELATIVE" --argjson id "$id" --argjson number "$number" --arg title "$TITLE" --arg url "$url" '. + [{implementationSubsection:$subsection,bodyFile:$body,id:$id,number:$number,title:$title,url:$url,body_verified:false,linked:false}]' "$LEDGER")"
atomic_write "$LEDGER" "$updated"
issue_json="$("$VERIFIER" "$REPO" "$number" "$BODY_FILE" 6 5 "$LOG_DIRECTORY/issue-$number-body-verification-failure.json")"
jq -e --argjson n "$number" '.number == $n' <<<"$issue_json" >/dev/null
update_ledger_flag "$number" body_verified true
linked=false
for attempt in 1 2 3; do
  if printf '{"sub_issue_id": %s}' "$id" | gh api "repos/$REPO/issues/$PARENT_ISSUE/sub_issues" -X POST --input - >/dev/null; then linked=true; break; fi
  sleep 2
done
$linked
update_ledger_flag "$number" linked true
jq -c '.[-1]' "$LEDGER"

```
{"implementationSubsection":"4.1 — Issue 1: Add the application-layer deadline change operation","bodyFile":"issue-bodies/01-4-1-body.md","id":5618788946,"number":2,"title":"4.1 — Add the application-layer deadline change operation","url":"https://github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01/issues/2","body_verified":true,"linked":true}
<shellId: 10 completed with exit code 0>
```


---

<sub>2m 1s</sub>

### `bash`

**Create and link second issue**

$ set -euo pipefail
REPO='edburns/dd-3070094-cargotracker-darwin-arm64-01'; PARENT_ISSUE=1
LOG_DIRECTORY='/Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339'
BODY_FILE="$LOG_DIRECTORY/issue-bodies/02-4-2-body.md"; BODY_RELATIVE='issue-bodies/02-4-2-body.md'
SUBSECTION='4.2 — Issue 2: Expose deadline changes through the booking facade'; TITLE='4.2 — Expose deadline changes through the booking facade'
LEDGER="$LOG_DIRECTORY/creation-ledger.json"; VERIFIER='/Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-github-issue-body.sh'
atomic_write() { local destination="$1"; local content="$2"; local temporary; temporary="$(mktemp "${destination}.tmp.XXXXXX")" || return 1; if ! printf '%s\n' "$content" > "$temporary"; then rm -f "$temporary"; return 1; fi; if ! mv "$temporary" "$destination"; then rm -f "$temporary"; return 1; fi; }
update_ledger_flag() { local number="$1" field="$2" value="$3" updated; updated="$(jq \
  --argjson number "$number" \
  --arg field "$field" \
  --argjson value "$value" \
  'map(if .number == $number then .[$field] = $value else . end)' \
  "$LEDGER")" || return 1; atomic_write "$LEDGER" "$updated"; }
created="$(gh api "repos/$REPO/issues" -X POST -f title="$TITLE" -F "body=@$BODY_FILE" --jq '{id,number,node_id,html_url,title}')"
id="$(jq -r '.id' <<<"$created")"; number="$(jq -r '.number' <<<"$created")"; url="$(jq -r '.html_url' <<<"$created")"
updated="$(jq --arg subsection "$SUBSECTION" --arg body "$BODY_RELATIVE" --argjson id "$id" --argjson number "$number" --arg title "$TITLE" --arg url "$url" '. + [{implementationSubsection:$subsection,bodyFile:$body,id:$id,number:$number,title:$title,url:$url,body_verified:false,linked:false}]' "$LEDGER")"; atomic_write "$LEDGER" "$updated"
issue_json="$("$VERIFIER" "$REPO" "$number" "$BODY_FILE" 6 5 "$LOG_DIRECTORY/issue-$number-body-verification-failure.json")"; jq -e --argjson n "$number" '.number == $n' <<<"$issue_json" >/dev/null; update_ledger_flag "$number" body_verified true
linked=false; for attempt in 1 2 3; do if printf '{"sub_issue_id": %s}' "$id" | gh api "repos/$REPO/issues/$PARENT_ISSUE/sub_issues" -X POST --input - >/dev/null; then linked=true; break; fi; sleep 2; done; $linked; update_ledger_flag "$number" linked true
jq -c '.[-1]' "$LEDGER"

```
{"implementationSubsection":"4.2 — Issue 2: Expose deadline changes through the booking facade","bodyFile":"issue-bodies/02-4-2-body.md","id":5618790510,"number":3,"title":"4.2 — Expose deadline changes through the booking facade","url":"https://github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01/issues/3","body_verified":true,"linked":true}
<shellId: 11 completed with exit code 0>
```


---

<sub>2m 9s</sub>

### `bash`

**Create and link third issue**

$ set -euo pipefail
REPO='edburns/dd-3070094-cargotracker-darwin-arm64-01'; PARENT_ISSUE=1
LOG_DIRECTORY='/Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339'
BODY_FILE="$LOG_DIRECTORY/issue-bodies/03-4-3-body.md"; BODY_RELATIVE='issue-bodies/03-4-3-body.md'
SUBSECTION='4.3 — Issue 3: Implement the deadline editor backing model'; TITLE='4.3 — Implement the deadline editor backing model'
LEDGER="$LOG_DIRECTORY/creation-ledger.json"; VERIFIER='/Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-github-issue-body.sh'
atomic_write() { local destination="$1"; local content="$2"; local temporary; temporary="$(mktemp "${destination}.tmp.XXXXXX")" || return 1; if ! printf '%s\n' "$content" > "$temporary"; then rm -f "$temporary"; return 1; fi; if ! mv "$temporary" "$destination"; then rm -f "$temporary"; return 1; fi; }
update_ledger_flag() { local number="$1" field="$2" value="$3" updated; updated="$(jq \
  --argjson number "$number" \
  --arg field "$field" \
  --argjson value "$value" \
  'map(if .number == $number then .[$field] = $value else . end)' \
  "$LEDGER")" || return 1; atomic_write "$LEDGER" "$updated"; }
created="$(gh api "repos/$REPO/issues" -X POST -f title="$TITLE" -F "body=@$BODY_FILE" --jq '{id,number,node_id,html_url,title}')"
id="$(jq -r '.id' <<<"$created")"; number="$(jq -r '.number' <<<"$created")"; url="$(jq -r '.html_url' <<<"$created")"
updated="$(jq --arg subsection "$SUBSECTION" --arg body "$BODY_RELATIVE" --argjson id "$id" --argjson number "$number" --arg title "$TITLE" --arg url "$url" '. + [{implementationSubsection:$subsection,bodyFile:$body,id:$id,number:$number,title:$title,url:$url,body_verified:false,linked:false}]' "$LEDGER")"; atomic_write "$LEDGER" "$updated"
issue_json="$("$VERIFIER" "$REPO" "$number" "$BODY_FILE" 6 5 "$LOG_DIRECTORY/issue-$number-body-verification-failure.json")"; jq -e --argjson n "$number" '.number == $n' <<<"$issue_json" >/dev/null; update_ledger_flag "$number" body_verified true
linked=false; for attempt in 1 2 3; do if printf '{"sub_issue_id": %s}' "$id" | gh api "repos/$REPO/issues/$PARENT_ISSUE/sub_issues" -X POST --input - >/dev/null; then linked=true; break; fi; sleep 2; done; $linked; update_ledger_flag "$number" linked true
jq -c '.[-1]' "$LEDGER"

```
{"implementationSubsection":"4.3 — Issue 3: Implement the deadline editor backing model","bodyFile":"issue-bodies/03-4-3-body.md","id":5618791942,"number":4,"title":"4.3 — Implement the deadline editor backing model","url":"https://github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01/issues/4","body_verified":true,"linked":true}
<shellId: 12 completed with exit code 0>
```


---

<sub>2m 19s</sub>

### `bash`

**Create and link fourth issue**

$ set -euo pipefail
REPO='edburns/dd-3070094-cargotracker-darwin-arm64-01'; PARENT_ISSUE=1
LOG_DIRECTORY='/Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339'
BODY_FILE="$LOG_DIRECTORY/issue-bodies/04-4-4-body.md"; BODY_RELATIVE='issue-bodies/04-4-4-body.md'
SUBSECTION='4.4 — Issue 4: Implement the PrimeFaces deadline dialog'; TITLE='4.4 — Implement the PrimeFaces deadline dialog'
LEDGER="$LOG_DIRECTORY/creation-ledger.json"; VERIFIER='/Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-github-issue-body.sh'
atomic_write() { local destination="$1"; local content="$2"; local temporary; temporary="$(mktemp "${destination}.tmp.XXXXXX")" || return 1; if ! printf '%s\n' "$content" > "$temporary"; then rm -f "$temporary"; return 1; fi; if ! mv "$temporary" "$destination"; then rm -f "$temporary"; return 1; fi; }
update_ledger_flag() { local number="$1" field="$2" value="$3" updated; updated="$(jq \
  --argjson number "$number" \
  --arg field "$field" \
  --argjson value "$value" \
  'map(if .number == $number then .[$field] = $value else . end)' \
  "$LEDGER")" || return 1; atomic_write "$LEDGER" "$updated"; }
created="$(gh api "repos/$REPO/issues" -X POST -f title="$TITLE" -F "body=@$BODY_FILE" --jq '{id,number,node_id,html_url,title}')"
id="$(jq -r '.id' <<<"$created")"; number="$(jq -r '.number' <<<"$created")"; url="$(jq -r '.html_url' <<<"$created")"
updated="$(jq --arg subsection "$SUBSECTION" --arg body "$BODY_RELATIVE" --argjson id "$id" --argjson number "$number" --arg title "$TITLE" --arg url "$url" '. + [{implementationSubsection:$subsection,bodyFile:$body,id:$id,number:$number,title:$title,url:$url,body_verified:false,linked:false}]' "$LEDGER")"; atomic_write "$LEDGER" "$updated"
issue_json="$("$VERIFIER" "$REPO" "$number" "$BODY_FILE" 6 5 "$LOG_DIRECTORY/issue-$number-body-verification-failure.json")"; jq -e --argjson n "$number" '.number == $n' <<<"$issue_json" >/dev/null; update_ledger_flag "$number" body_verified true
linked=false; for attempt in 1 2 3; do if printf '{"sub_issue_id": %s}' "$id" | gh api "repos/$REPO/issues/$PARENT_ISSUE/sub_issues" -X POST --input - >/dev/null; then linked=true; break; fi; sleep 2; done; $linked; update_ledger_flag "$number" linked true
jq -c '.[-1]' "$LEDGER"

```
{"implementationSubsection":"4.4 — Issue 4: Implement the PrimeFaces deadline dialog","bodyFile":"issue-bodies/04-4-4-body.md","id":5618793775,"number":5,"title":"4.4 — Implement the PrimeFaces deadline dialog","url":"https://github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01/issues/5","body_verified":true,"linked":true}
<shellId: 13 completed with exit code 0>
```


---

<sub>2m 30s</sub>

### `bash`

**Create and link fifth issue**

$ set -euo pipefail
REPO='edburns/dd-3070094-cargotracker-darwin-arm64-01'; PARENT_ISSUE=1
LOG_DIRECTORY='/Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339'
BODY_FILE="$LOG_DIRECTORY/issue-bodies/05-4-5-body.md"; BODY_RELATIVE='issue-bodies/05-4-5-body.md'
SUBSECTION='4.5 — Issue 5: Integrate deadline editing into the Administration dashboard'; TITLE='4.5 — Integrate deadline editing into the Administration dashboard'
LEDGER="$LOG_DIRECTORY/creation-ledger.json"; VERIFIER='/Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-github-issue-body.sh'
atomic_write() { local destination="$1"; local content="$2"; local temporary; temporary="$(mktemp "${destination}.tmp.XXXXXX")" || return 1; if ! printf '%s\n' "$content" > "$temporary"; then rm -f "$temporary"; return 1; fi; if ! mv "$temporary" "$destination"; then rm -f "$temporary"; return 1; fi; }
update_ledger_flag() { local number="$1" field="$2" value="$3" updated; updated="$(jq \
  --argjson number "$number" \
  --arg field "$field" \
  --argjson value "$value" \
  'map(if .number == $number then .[$field] = $value else . end)' \
  "$LEDGER")" || return 1; atomic_write "$LEDGER" "$updated"; }
created="$(gh api "repos/$REPO/issues" -X POST -f title="$TITLE" -F "body=@$BODY_FILE" --jq '{id,number,node_id,html_url,title}')"
id="$(jq -r '.id' <<<"$created")"; number="$(jq -r '.number' <<<"$created")"; url="$(jq -r '.html_url' <<<"$created")"
updated="$(jq --arg subsection "$SUBSECTION" --arg body "$BODY_RELATIVE" --argjson id "$id" --argjson number "$number" --arg title "$TITLE" --arg url "$url" '. + [{implementationSubsection:$subsection,bodyFile:$body,id:$id,number:$number,title:$title,url:$url,body_verified:false,linked:false}]' "$LEDGER")"; atomic_write "$LEDGER" "$updated"
issue_json="$("$VERIFIER" "$REPO" "$number" "$BODY_FILE" 6 5 "$LOG_DIRECTORY/issue-$number-body-verification-failure.json")"; jq -e --argjson n "$number" '.number == $n' <<<"$issue_json" >/dev/null; update_ledger_flag "$number" body_verified true
linked=false; for attempt in 1 2 3; do if printf '{"sub_issue_id": %s}' "$id" | gh api "repos/$REPO/issues/$PARENT_ISSUE/sub_issues" -X POST --input - >/dev/null; then linked=true; break; fi; sleep 2; done; $linked; update_ledger_flag "$number" linked true
jq -c '.[-1]' "$LEDGER"

```
{"implementationSubsection":"4.5 — Issue 5: Integrate deadline editing into the Administration dashboard","bodyFile":"issue-bodies/05-4-5-body.md","id":5618795888,"number":6,"title":"4.5 — Integrate deadline editing into the Administration dashboard","url":"https://github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01/issues/6","body_verified":true,"linked":true}
<shellId: 14 completed with exit code 0>
```


---

<sub>2m 35s</sub>

### Copilot

All five issues were created, exact-body verified, and linked successfully in order. I’m performing the authoritative child-link comparison and final independent checks that every issue remains open, unassigned, and body-identical before marking the stage complete.

---

<sub>2m 39s</sub>

### `bash`

**Verify final child links**

$ set -euo pipefail
REPO='edburns/dd-3070094-cargotracker-darwin-arm64-01'
LOG_DIRECTORY='/Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339'
FINAL="$LOG_DIRECTORY/final-children.json"
atomic_write() {
  local destination="$1"
  local content="$2"
  local temporary
  temporary="$(mktemp "${destination}.tmp.XXXXXX")" || return 1
  if ! printf '%s\n' "$content" > "$temporary"; then rm -f "$temporary"; return 1; fi
  if ! mv "$temporary" "$destination"; then rm -f "$temporary"; return 1; fi
}
children_pages="$(gh api "repos/$REPO/issues/1/sub_issues" --paginate --slurp)"
children_flat="$(printf '%s\n' "$children_pages" | jq 'if length == 0 then [] elif all(.[]; type == "array") then add else . end')"
atomic_write "$FINAL" "$children_flat"
'/Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-stage20-child-links.sh' \
  "$LOG_DIRECTORY/pre-creation-children.json" \
  "$FINAL" \
  "$LOG_DIRECTORY/creation-ledger.json"

<details>
<summary>13 lines</summary>

```
{
  "baselineCount": 0,
  "ledgerCount": 5,
  "finalCount": 5,
  "newIssueIds": [
    5618788946,
    5618790510,
    5618791942,
    5618793775,
    5618795888
  ]
}
<shellId: 15 completed with exit code 0>
```

</details>


---

<sub>2m 49s</sub>

### `bash`

**Verify first issue final state**

$ set -euo pipefail
LOG='/Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339'; V='/Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-github-issue-body.sh'; j="$("$V" 'edburns/dd-3070094-cargotracker-darwin-arm64-01' 2 "$LOG/issue-bodies/01-4-1-body.md" 6 5 "$LOG/issue-2-final-body-verification-failure.json")"; jq -e '.state == "open" and (.assignees | type == "array" and length == 0)' <<<"$j" >/dev/null; printf 'issue-2-final-ok\n'

```
issue-2-final-ok
<shellId: 16 completed with exit code 0>
```


---

<sub>2m 49s</sub>

### `bash`

**Verify second issue final state**

$ set -euo pipefail
LOG='/Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339'; V='/Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-github-issue-body.sh'; j="$("$V" 'edburns/dd-3070094-cargotracker-darwin-arm64-01' 3 "$LOG/issue-bodies/02-4-2-body.md" 6 5 "$LOG/issue-3-final-body-verification-failure.json")"; jq -e '.state == "open" and (.assignees | type == "array" and length == 0)' <<<"$j" >/dev/null; printf 'issue-3-final-ok\n'

```
issue-3-final-ok
<shellId: 17 completed with exit code 0>
```


---

<sub>2m 49s</sub>

### `bash`

**Verify third issue final state**

$ set -euo pipefail
LOG='/Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339'; V='/Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-github-issue-body.sh'; j="$("$V" 'edburns/dd-3070094-cargotracker-darwin-arm64-01' 4 "$LOG/issue-bodies/03-4-3-body.md" 6 5 "$LOG/issue-4-final-body-verification-failure.json")"; jq -e '.state == "open" and (.assignees | type == "array" and length == 0)' <<<"$j" >/dev/null; printf 'issue-4-final-ok\n'

```
issue-4-final-ok
<shellId: 18 completed with exit code 0>
```


---

<sub>2m 49s</sub>

### `bash`

**Verify fourth issue final state**

$ set -euo pipefail
LOG='/Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339'; V='/Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-github-issue-body.sh'; j="$("$V" 'edburns/dd-3070094-cargotracker-darwin-arm64-01' 5 "$LOG/issue-bodies/04-4-4-body.md" 6 5 "$LOG/issue-5-final-body-verification-failure.json")"; jq -e '.state == "open" and (.assignees | type == "array" and length == 0)' <<<"$j" >/dev/null; printf 'issue-5-final-ok\n'

```
issue-5-final-ok
<shellId: 19 completed with exit code 0>
```


---

<sub>2m 49s</sub>

### `bash`

**Verify fifth issue final state**

$ set -euo pipefail
LOG='/Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339'; V='/Users/edburns/.copilot/plugins/shepherd-task/scripts/verify-github-issue-body.sh'; j="$("$V" 'edburns/dd-3070094-cargotracker-darwin-arm64-01' 6 "$LOG/issue-bodies/05-4-5-body.md" 6 5 "$LOG/issue-6-final-body-verification-failure.json")"; jq -e '.state == "open" and (.assignees | type == "array" and length == 0)' <<<"$j" >/dev/null; printf 'issue-6-final-ok\n'

```
issue-6-final-ok
<shellId: 20 completed with exit code 0>
```


---

<sub>2m 57s</sub>

### `bash`

**Finalize successful stage result**

$ set -euo pipefail
LOG_DIRECTORY='/Users/edburns/workareas/dd-3070094-cargotracker-darwin-arm64-01-shepherd-control/1-arrival-deadline-control-remove-before-merge/prompts/shepherd-task-20-20260928-1339'
LEDGER="$LOG_DIRECTORY/creation-ledger.json"
RESULT="$LOG_DIRECTORY/stage-20-result.json"
atomic_write() {
  local destination="$1"
  local content="$2"
  local temporary
  temporary="$(mktemp "${destination}.tmp.XXXXXX")" || return 1
  if ! printf '%s\n' "$content" > "$temporary"; then rm -f "$temporary"; return 1; fi
  if ! mv "$temporary" "$destination"; then rm -f "$temporary"; return 1; fi
}
jq -e 'type == "array" and length == 5 and all(.[]; .body_verified == true and .linked == true)' "$LEDGER" >/dev/null
atomic_write "$RESULT" '{"schemaVersion":1,"status":"complete","ledgerFile":"creation-ledger.json","operationError":null}'
jq -e '.schemaVersion == 1 and .status == "complete" and .ledgerFile == "creation-ledger.json" and .operationError == null' "$RESULT" >/dev/null
jq -r '.[] | [.implementationSubsection, (.number|tostring), .title, .url] | @tsv' "$LEDGER"

<details>
<summary>6 lines</summary>

```
4.1 — Issue 1: Add the application-layer deadline change operation	2	4.1 — Add the application-layer deadline change operation	https://github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01/issues/2
4.2 — Issue 2: Expose deadline changes through the booking facade	3	4.2 — Expose deadline changes through the booking facade	https://github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01/issues/3
4.3 — Issue 3: Implement the deadline editor backing model	4	4.3 — Implement the deadline editor backing model	https://github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01/issues/4
4.4 — Issue 4: Implement the PrimeFaces deadline dialog	5	4.4 — Implement the PrimeFaces deadline dialog	https://github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01/issues/5
4.5 — Issue 5: Integrate deadline editing into the Administration dashboard	6	4.5 — Integrate deadline editing into the Administration dashboard	https://github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01/issues/6
<shellId: 21 completed with exit code 0>
```

</details>


---

<sub>3m 0s</sub>

### Copilot

Created and linked five ordered, unassigned child issues under #1. All were created **without an issue type** because the repository owner is a user account.

| Subsection | Issue | Title |
|---|---:|---|
| 4.1 | [#2](https://github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01/issues/2) | Add the application-layer deadline change operation |
| 4.2 | [#3](https://github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01/issues/3) | Expose deadline changes through the booking facade |
| 4.3 | [#4](https://github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01/issues/4) | Implement the deadline editor backing model |
| 4.4 | [#5](https://github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01/issues/5) | Implement the PrimeFaces deadline dialog |
| 4.5 | [#6](https://github.com/edburns/dd-3070094-cargotracker-darwin-arm64-01/issues/6) | Integrate deadline editing into the Administration dashboard |

**Given list:** `2, 3, 4, 5, 6`

**Suggested stage 25 invocation:** Invoke `shepherd-task-25-given-list` with `REPO=edburns/dd-3070094-cargotracker-darwin-arm64-01`, `BASE_BRANCH=experiment/shepherd-control`, `ISSUE_NUMBERS=2,3,4,5,6`, and `PLAN_DIRECTORY=1-arrival-deadline-control-remove-before-merge`.

---

<sub>Generated by [GitHub Copilot CLI](https://github.com/features/copilot/cli)</sub>
