# Publishing to Maven Central

## Prerequisites

1. Sonatype account at https://central.sonatype.com/
2. Verified namespace `io.github.space0032`
3. GPG key pair generated and public key published

## GitHub Secrets Required

Add these secrets to your repository settings:

| Secret Name | Description |
|-------------|-------------|
| `MAVEN_CENTRAL_USERNAME` | Sonatype token username (from central.sonatype.com/account) |
| `MAVEN_CENTRAL_PASSWORD` | Sonatype token password |
| `GPG_PRIVATE_KEY` | Your GPG private key (armor format) |
| `GPG_PASSPHRASE` | Your GPG key passphrase |

### Getting Your Sonatype Token

1. Go to https://central.sonatype.com/account
2. Click "Generate User Token"
3. Copy the username and password values

### Exporting Your GPG Private Key

```bash
gpg --export-secret-keys --armor YOUR_KEY_ID > private-key.asc
cat private-key.asc
# Copy the entire output including BEGIN/END lines
```

## Publishing a New Version

### Automatic (Recommended)

1. Update version in `pom.xml`
2. Commit and push changes
3. Create a new GitHub Release with tag (e.g., `v1.0.0`)
4. GitHub Actions will automatically publish to Maven Central

### Manual

Run the workflow manually from GitHub Actions tab with optional version input.

## Verification

After publishing, your artifact will be available at:
- https://search.maven.org/artifact/io.github.space0032/java-validation-sanitizer
- https://central.sonatype.com/artifact/io.github.space0032/java-validation-sanitizer

Note: It may take 30 minutes to a few hours for the artifact to appear in search results.
