# allcom-backend

### Code Style Checks

To ensure that the code meets our style standards, run the Checkstyle plugin with Maven:

```bash
mvn checkstyle:check pmd:check
```

For a concise summary of warnings and errors:

```bash
mvn checkstyle:check pmd:check | grep '\[WARN\]\|\[ERROR\]\|\[INFO\] PMD Failure'
```

#### Setting up a Pre-commit Hook

We use a pre-commit hook to automatically check code style before each commit.
Run the following command to create a pre-commit file in your .git/hooks directory and set the necessary permissions:

```bash
echo '#!/bin/sh\nmvn checkstyle:check pmd:check\nif [ $? -ne 0 ]; then\n  echo "Checkstyle violations found. Commit aborted."\n  exit 1\nfi' > .git/hooks/pre-commit && chmod +x .git/hooks/pre-commit
```
