import os

replacements = {
    "zephyr-framework/zephyr-core/pom.xml": [
        ("<artifactId>zephyr-base</artifactId>", "<artifactId>zephyr-framework</artifactId>")
    ],
    "zephyr-framework/zephyr-starters/zephyr-starter-api/pom.xml": [
        ("<artifactId>zephyr-starter</artifactId>", "<artifactId>zephyr-starters</artifactId>")
    ],
    "zephyr-framework/zephyr-starters/zephyr-starter-auth/pom.xml": [
        ("<artifactId>zephyr-starter</artifactId>", "<artifactId>zephyr-starters</artifactId>")
    ],
    "zephyr-framework/zephyr-starters/zephyr-starter-jwt/pom.xml": [
        ("<artifactId>zephyr-starter</artifactId>", "<artifactId>zephyr-starters</artifactId>")
    ],
    "zephyr-framework/zephyr-starters/zephyr-starter-redis/pom.xml": [
        ("<artifactId>zephyr-starter</artifactId>", "<artifactId>zephyr-starters</artifactId>")
    ],
    "zephyr-framework/zephyr-starters/zephyr-starter-runner/pom.xml": [
        ("<artifactId>zephyr-starter</artifactId>", "<artifactId>zephyr-starters</artifactId>")
    ],
    "zephyr-framework/zephyr-starters/zephyr-starter-vault/pom.xml": [
        ("<artifactId>zephyr-starter</artifactId>", "<artifactId>zephyr-starters</artifactId>")
    ],
    "zephyr-framework/zephyr-starters/zephyr-starter-mybatis/pom.xml": [
        ("<artifactId>zephyr-starter</artifactId>", "<artifactId>zephyr-starters</artifactId>"),
        ("<artifactId>zephyr-starter-mp</artifactId>", "<artifactId>zephyr-starter-mybatis</artifactId>")
    ],
    "zephyr-platform/zephyr-platform-service/pom.xml": [
        ("<artifactId>zephyr-admin</artifactId>", "<artifactId>zephyr-platform</artifactId>"),
        ("<artifactId>admin-boot</artifactId>", "<artifactId>zephyr-platform-service</artifactId>")
    ],
    "zephyr-platform/zephyr-platform-system/pom.xml": [
        ("<artifactId>zephyr-admin</artifactId>", "<artifactId>zephyr-platform</artifactId>"),
        ("<artifactId>zephyr-system</artifactId>", "<artifactId>zephyr-platform-system</artifactId>")
    ],
    "zephyr-platform/zephyr-platform-auth/pom.xml": [
        ("<artifactId>zephyr-admin</artifactId>", "<artifactId>zephyr-platform</artifactId>"),
        ("<artifactId>zephyr-auth</artifactId>", "<artifactId>zephyr-platform-auth</artifactId>")
    ],
    "zephyr-platform/zephyr-platform-ops/pom.xml": [
        ("<artifactId>zephyr-admin</artifactId>", "<artifactId>zephyr-platform</artifactId>"),
        ("<artifactId>zephyr-ops</artifactId>", "<artifactId>zephyr-platform-ops</artifactId>")
    ],
    "zephyr-platform/zephyr-platform-support/pom.xml": [
        ("<artifactId>zephyr-admin</artifactId>", "<artifactId>zephyr-platform</artifactId>"),
        ("<artifactId>zephyr-support</artifactId>", "<artifactId>zephyr-platform-support</artifactId>")
    ],
    "zephyr-business/zephyr-business-service/pom.xml": [
        ("<artifactId>zephyr-biz</artifactId>", "<artifactId>zephyr-business</artifactId>"),
        ("<artifactId>biz-boot</artifactId>", "<artifactId>zephyr-business-service</artifactId>")
    ],
    "zephyr-platform/zephyr-platform-system/zephyr-system-api/pom.xml": [
        ("<artifactId>zephyr-system</artifactId>", "<artifactId>zephyr-platform-system</artifactId>")
    ],
    "zephyr-platform/zephyr-platform-system/zephyr-system-biz/pom.xml": [
        ("<artifactId>zephyr-system</artifactId>", "<artifactId>zephyr-platform-system</artifactId>")
    ],
    "zephyr-platform/zephyr-platform-auth/zephyr-auth-api/pom.xml": [
        ("<artifactId>zephyr-auth</artifactId>", "<artifactId>zephyr-platform-auth</artifactId>")
    ],
    "zephyr-platform/zephyr-platform-auth/zephyr-auth-biz/pom.xml": [
        ("<artifactId>zephyr-auth</artifactId>", "<artifactId>zephyr-platform-auth</artifactId>")
    ],
    "zephyr-platform/zephyr-platform-support/zephyr-generator/pom.xml": [
        ("<artifactId>zephyr-support</artifactId>", "<artifactId>zephyr-platform-support</artifactId>")
    ],
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

