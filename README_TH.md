
# 🔌 ปลั๊กอิน bsruCrates

<div align="center">

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![GitHub Repo stars](https://img.shields.io/github/stars/Nattapat2871/BsruCrates?style=flat-square)](https://github.com/Nattapat2871/BsruCrates/stargazers)
![Visitor Badge](https://api.visitorbadge.io/api/VisitorHit?user=Nattapat2871&repo=BsruCratess&countColor=%237B1E7A&style=flat-square)

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/Nattapat2871)

</div>

<p align= "center">
        <a href="/README.md">English</a>   <b>ภาษาไทย</b>　

ปลั๊กอินระบบกล่อง (Crate) ที่ยืดหยุ่นและทรงพลังสำหรับเซิร์ฟเวอร์ Spigot/Paper ที่ทันสมัย bsruCrates แตกต่างจากปลั๊กอินกล่องสุ่มทั่วไป โดยให้ผู้เล่นสามารถ **เลือกของรางวัลที่ต้องการได้เอง** จากเมนู GUI มาพร้อมกับระบบกุญแจแบบคู่ที่โดดเด่น ทำให้สามารถเปิดกล่องได้ทั้งจากกุญแจที่เป็นไอเทม หรือใช้แต้มเสมือนจริงก็ได้

ปลั๊กอินนี้ถูกออกแบบมาเพื่อเจ้าของเซิร์ฟเวอร์ที่ต้องการมอบรางวัลที่มีคุณค่าและเฉพาะเจาะจง (รวมถึงไอเทมพิเศษจากปลั๊กอินอื่น) ให้กับผู้เล่นในรูปแบบที่สมดุลและน่าสนใจ

---
## ✨ ฟีเจอร์เด่น (Features)

- **เลือกของรางวัลได้เอง:** แทนที่จะสุ่มดวง ผู้เล่นสามารถคลิกขวาที่กล่องและเลือกของรางวัลที่ต้องการได้เองจากเมนู
- **ระบบกุญแจแบบคู่ (Dual-Key System):** ทุกกล่องสามารถเปิดได้ 2 วิธี โดยระบบจะเช็คกุญแจไอเทมก่อนเสมอ:
    1.  **กุญแจไอเทม (Physical Keys):** ไอเทมพิเศษที่มีข้อมูล NBT เฉพาะตัว ไม่สามารถปลอมแปลงได้ แอดมินสามารถเสกให้ผู้เล่นได้
    2.  **ระบบแต้ม (Points System):** แต้มเสมือนจริงที่ถูกเก็บแยกตามผู้เล่นและตามประเภทของกล่อง เหมาะสำหรับเป็นรางวัลจากการโหวต, เควส, หรือระบบเศรษฐกิจของเซิร์ฟเวอร์
- **ระบบจัดการกล่องในเกม:**
    - **`/bsrucrates additem <type> <slot>`:** เพิ่มไอเทมชิ้นไหนก็ได้จากในมือของคุณลงไปในเมนูกล่องโดยตรง โดยจะเก็บข้อมูลชื่อ, lore, enchant, และ NBT จากปลั๊กอินอื่นไปทั้งหมด
    - **`/bsrucrates removeitem <type> <slot>`:** ลบของรางวัลออกจากเมนูได้อย่างง่ายดาย
- **หน้าต่างยืนยัน 2 ชั้น:** มีหน้าต่างให้กดยืนยันเพื่อป้องกันการกดพลาด ทำให้ผู้เล่นมั่นใจในการแลกของ
- **คำสั่งแอดมินครบครัน:** ชุดคำสั่งที่ครอบคลุมสำหรับจัดการทุกส่วนของปลั๊กอิน:
    - สร้าง/ลบ บล็อกกล่องในโลก
    - แจกกุญแจไอเทม
    - จัดการแต้มผู้เล่น (ให้/ตั้งค่า/ลบ)
    - รีโหลดคอนฟิกทั้งหมดได้ทันที
- **ปรับแต่งได้สูง:** ปรับแต่งข้อความ, ชื่อ GUI, และเสียงประกอบทั้งหมดได้ตามต้องการ
- **รองรับ PlaceholderAPI:** แสดงจำนวนกุญแจและแต้มของผู้เล่นได้ทุกที่ในเซิร์ฟเวอร์

---
## 🎮 เวอร์ชั่นที่รองรับ (Compatibility)

- **Minecraft Version:** `1.18` - `1.21+`
- **Server Software:** Spigot, Paper, และเวอร์ชันแยก (forks) อื่นๆ

---
## 🛠️ การติดตั้ง (Installation)

1.  ดาวน์โหลดไฟล์ `.jar` ล่าสุดจากหน้า [Releases](https://github.com/Nattapat2871/BsruCrates/releases)
2.  นำไฟล์ `.jar` ที่ดาวน์โหลดไปใส่ในโฟลเดอร์ `plugins` ของเซิร์ฟเวอร์
3.  (ทางเลือก แนะนำ) ติดตั้ง [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.624/)
4.  รีสตาร์ทเซิร์ฟเวอร์ของคุณ
5.  ปลั๊กอินจะสร้างไฟล์ `config.yml`, `crates.yml`, และไฟล์อื่นๆ ที่จำเป็นให้คุณเข้าไปตั้งค่า

---
## 📋 คำสั่งและสิทธิ์ (Commands & Permissions)

คำสั่งแอดมินทั้งหมดต้องการสิทธิ์ `bsrucrates.admin` ส่วนการใช้งานของผู้เล่นต้องการ `bsrucrates.use`

| คำสั่ง | คำอธิบาย |
| :--- | :--- |
| `/bsrucrates` | แสดงข้อมูลปลั๊กอิน |
| `/bsrucrates help` | แสดงคำสั่งทั้งหมด |
| `/bsrucrates reload` | รีโหลดไฟล์คอนฟิกทั้งหมด |
| `/bsrucrates key give <player> <type> [amount]` | แจกกุญแจไอเทม |
| `/bsrucrates points <give\|set\|take> <player> <type> <amount>`| จัดการแต้มของผู้เล่น |
| `/bsrucrates set <type>` | ตั้งค่าบล็อกที่มองอยู่ให้เป็นกล่อง |
| `/bsrucrates remove` | ลบกล่องที่มองอยู่ |
| `/bsrucrates additem <type> <slot>` | เพิ่มของที่ถืออยู่เข้ากล่อง |
| `/bsrucrates removeitem <type> <slot>` | ลบของออกจากกล่อง |

---
## 🔌 Placeholders (PlaceholderAPI)

- `%bsrucrates_physicalkeys_<ประเภทกล่อง>%`
    - แสดงจำนวน **กุญแจ (ไอเทม)** ที่ผู้เล่นมีสำหรับกล่องนั้นๆ
    - **ตัวอย่าง:** `%bsrucrates_physicalkeys_vote%`

- `%bsrucrates_points_<ประเภทกล่อง>%`
    - แสดงจำนวน **แต้ม** ที่ผู้เล่นมีสำหรับกล่องนั้นๆ
    - **ตัวอย่าง:** `%bsrucrates_points_vote%`

---
## ⚙️ การตั้งค่า (`config.yml`)

ไฟล์นี้ใช้สำหรับควบคุมข้อความและเสียงประกอบทั้งหมดในปลั๊กอิน

```yaml
messages:
  prefix: "&8[&bCrates&8] "
  no_permission: "&cคุณไม่มีสิทธิ์ใช้งานคำสั่งนี้"
  player_not_found: "&cไม่พบผู้เล่น '{player}'"
  crate_type_not_found: "&cไม่พบประเภทของกล่องที่ชื่อว่า '{type}'"
  key_given: "&aคุณได้ให้ {amount}x {key_name} &aแก่ {player}"
  key_received: "&aคุณได้รับ {amount}x {key_name}&a!"
  inventory_full: "&cช่องเก็บของของ {player} เต็ม ของจึงถูกดรอปลงบนพื้น"
  set_crate_success: "&aตั้งค่าบล็อกที่มองอยู่ให้เป็นกล่อง '{type}' สำเร็จ"
  remove_crate_success: "&aลบกล่อง ณ ตำแหน่งนี้สำเร็จ"
  not_a_crate: "&cบล็อกนี้ไม่ใช่กล่อง"
  look_at_block: "&cคุณต้องมองไปที่บล็อก"
  no_key: "&cคุณมีกุญแจหรือแต้มไม่เพียงพอสำหรับเปิดกล่องนี้!"
  reward_received: "&aคุณได้รับ {reward_name}&a!"
  reload_success: "&aรีโหลดคอนฟิกทั้งหมดสำเร็จ!"

sounds:
  select_item: "ui.button.click"
  cancel_purchase: "entity.villager.no"
  reward_received: "entity.experience_orb.pickup"
  no_key: "entity.villager.no" 
```

---
## 🧑‍💻 Author
* Nattapat2871
* GitHub: https://github.com/Nattapat2871/BsruCrates