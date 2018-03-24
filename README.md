# PromoteOneself
## Description 
A plugin for Minecraft Spigot version 1.8. This plugin enables server administrators to set a series of targets for player to achieve in order to gain rewards, such as rank-ups (when a player meats a target, the plugin can be configured to run a set of commands). Targets are composed of aims; a player achieves a target upon completing all aims. Aims are achieved by, for example, gaining experience, gaining points, clicking certain configurable signs , etc. This plugin also has integration with the 'Valut' plugin and the 'PlayerPoints' plugin. 

--- This plugin curuently contains a number of debug commands; these should be removed before the plugin is built as they are a security problem. This plugin is made using eclipse; the .classpath file is excluded from this repository so should should be recreated --- 

This plugin has a number of commands, permissions and config files. 

This plugin is under a MIT license (see the LICENSE file). 

## Aims: 
Aims can have any of the following types: 
 - Points 
 - Money 
 - Password
 - Admin assigned 
 - Kill count 

## Commands: 
 - /prom 
 - /posset 
 - /promoteoneself
 
## Permissions: 
 - promoteoneself.* 

## Config files: 
 - config.yml 
 - signs.yml 
 - players.yml 

## Signs: 
Signs are written in the form: 
line 1: [pos]
line2: <type>
line3: <data>
line4: <sign id as in signs.yml> 

Signs can have any of the following types: 
 - Update 
 - Points 
 - Target 
 - Sign 

