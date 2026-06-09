import os

# Update pom.xml contents
replacements = {
    "zephyr-api/zephyr-api-system/pom.xml": [
        ("<artifactId>zephyr-platform-system</artifactId>", "<artifactId>zephyr-api</artifactId>"),
        ("<artifactId>zephyr-system-api</artifactId>", "<artifactId>zephyr-api-system</artifactId>")
    ],
    "zephyr-api/zephyr-api-auth/pom.xml": [
        ("<artifactId>zephyr-platform-auth</artifactId>", "<artifactId>zephyr-api</artifactId>"),
        ("<artifactId>zephyr-auth-api</artifactId>", "<artifactId>zephyr-api-auth</artifactId>"),
        ("<artifactId>zephyr-system-api</artifactId>", "<artifactId>zephyr-api-system</artifactId>")
    ],
    "zephyr-platform/zephyr-platform-system/pom.xml": [
        ("        <module>zephyr-system-api</module>\n", "")
    ],
    "zephyr-platform/zephyr-platform-auth/pom.xml": [
        ("        <module>zephyr-auth-api</module>\n", "")
    ],
    "zephyr-platform/zephyr-platform-system/zephyr-system-biz/pom.xml": [
        ("<artifactId>zephyr-system-api</artifactId>", "<artifactId>zephyr-api-system</artifactId>"),
        ("<artifactId>zephyr-auth-api</artifactId>", "<artifactId>zephyr-api-auth</artifactId>")
    ],
    "zephyr-platform/zephyr-platform-auth/zephyr-auth-biz/pom.xml": [
        ("<artifactId>zephyr-system-api</artifactId>", "<artifactId>zephyr-api-system</artifactId>"),
        ("<artifactId>zephyr-auth-api</artifactId>", "<artifactId>zephyr-api-auth</artifactId>")
    ]
}

for path, changes in replacements.items():
    if os.path.exists(path):
        with open(path, "r") as f:
            content = f.read()
        for old, new in changes:
            content = content.replace(old, new)
        with open(path, "w") as f:
            f.write(content)
        print(f"Updated {path}")
    else:
        print(f"File not found: {path}")

