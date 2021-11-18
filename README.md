# JustAttribute

> 作者: justwei

玩家属性插件

## 指令
```text
  /attribute - 查看全部
```
## 装备品质
> 节点: justattribute.quality

| ID | 显示名 | 描述 |
| :-----| :----: | :----: |
| 0 | C | 一无是处 |
| 1 | B | 十分常见 |
| 2 | A | 百里挑一 |
| 3 | A+ | 千载难逢 |
| 4 | S | 万众瞩目 |
| 5 | SS | 王者无敌 |
| 6 | SSS | 超凡入圣 |

## 装备类型
> 节点: justattribute.classify

| ID | 显示名 |
| :-----| :----: |
| 0 | 武器 |
| 1 | 副手 |
| 2 | 防具 |
| 3 | 眼镜 |
| 4 | 耳坠 |
| 5 | 披风 |
| 6 | 戒指 |
| 7 | 勋章 |
| 8 | 头衔 |
| 9 | 宠物蛋 |
| 10 | 宠物鞍 |
| 11 | 宠物装备 |

## 灵魂绑定
> 节点: justattribute.soulbound.action

| ID | 描述 |
| :-----| :----: |
| 0 | 自动绑定 |
| 1 | 使用后绑定 |

## 潜能评级
> 节点: justattribute.potency.grade

| ID | 符号 | 名称 |
| :-----| :----: | :----: |
| -1 | ㅐ | None |
| 0 | ㅑ | B |
| 1 | ㅒ | A |
| 2 | ㅓ | S |
| 3 | ㅔ | SS |
| 4 | ㅏ | SSS |

## zaphkiel item example
```yaml
example_item:
  display: example_display
  icon: WOODEN_SWORD
  name:
    NAME: '&7测试武器'
  lore:
    DESC:
      - '&f用来测试的武器.'
  data:
    justattribute:
      quality: 5
      soulbound:
        action: 0
      classify: 0
      ordinary:
        energy: 999
        stamina: 999
        wisdom: 999
        technique: 999
        damage: 999
        defence: 999
        health: 999
        mana: 999
        critical_chance: 0.2
        critical_damage: 0.75
      potency:
        grade: 1
        addition:
          damage: 0.06
          defence: 0.07
          helath: 0.1
          mana: 0.1
          restore_health: 2
          restore_mana: 2
          vampire_damage: 0.2
          vampire_versatile: 0.05
          defence_penetration: 0.2
          damage_immune: 0.1
          exp_addition: 1
```

## zaphkiel display example
```yaml
example_display:
  name: '&7<NAME>'
  lore:
    - '<display.quality...>'
    - '<display.soulbound...>'
    - '<display.combat...>'
    - '<display.classify...>'
    - '<display.realm...>'
    - ''
    - '<display.ordinary...>'
    - ''
    - '<display.potency...>'
    - ''
    - '&f<DESC...>'
```