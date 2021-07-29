# Kotlin 模板

> 一套简单的 Kotlin DSL Gradle 项目

## 特性

- 跟随`Gradle` 和 `Kotlin` 最新稳定版本
- 支持 `Java Module`
- ~~针对中华人民共和国配置`Maven`加速镜像~~

## 快速开始

1. 从 `https://github.com/OpenEdgn/GradleKotlinTemplate` 克隆项目
2. 修改[LICENSE](./LICENSE) 为合适的分发协议
3. 重建 [README.md](./README.md) 文件
4. 修改 [settings.gradle.kts](./settings.gradle.kts) 中 `project.name` 为合适的名称
5. 在项目根目录下执行 `./gradlew build -x test`
6. 在项目根目录下执行 `./gradlew test`
7. 删除 [template](./template) 下的模板代码
8. 修改 [module-info.java](./template/src/main/java/module-info.java) 中的配置

## 更新日志

- **20210720**
  - 更新 `Kolin` 版本至 `1.5.21`
  - 更新 `Gradle` 版本至 `7.1.1`
  - 去除第三方不可信 `Maven` 源以降低 [供应链攻击](https://en.wikipedia.org/wiki/Supply_chain_attack) 风险
  - 重命名默认子模块名称

- **20210322**
    - 更新 `Kolin` 版本至 `1.4.31`
    - 更新 `Gradle` 版本至 `6.8.3`
    - 去除 `resources` 下孤立文件
    - 将插件版本配置合并到 [gradle.properties](./gradle.properties) 下

- **20201208**
    - 新增 Java 9 模块化支持
    - 修复在使用`maven-publish`打包时未对源码打包的问题

- **20201125**
    - 更改项目名称为 `GradleKotlinTemplate`
    - 添加 `maven-publish` 插件，现在可以使用 `gradlew publishToMavenLocal` 了
    - 修改 `Kotlin` 插件编译生成目录 [build.gradle.kts](./template/build.gradle.kts)

- **20201120**
    - 更新 `kotlin` 为 `1.4.10`
    - 更新 `gradle` 为 `6.5.1`
    - 删除过时的 `buildSrc` 方案

## LICENSE

请看 [LICENSE](./LICENSE) 来了解此模板的 LICENSE
