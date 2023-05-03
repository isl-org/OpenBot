# ==========================================
# Name : racer.py
# Desc : Abstraction of the physical car
# setting the traits of the car corresponds
# to setting them on the physical car
# ==========================================
import traitlets


class Racecar(traitlets.HasTraits):
    """Racecar class -> abstracted class for validating steering and throttle"""

    steering = traitlets.Float()
    throttle = traitlets.Float()
    # thr_scale = traitlets.Float()

    @traitlets.observe("steering")
    def _clip_steering(self, proposal):
        """clips the steering if it exceeds the range [-1.0,1.0]
        Parameters:
        ----------
        proposal : traitlets
            Proposed value for the steering
        Returns:
        --------
        traitlets
            Validated value for steering
        """
        if proposal["new"] > 1.0:
            return 1.0
        elif proposal["new"] < -1.0:
            return -1.0
        else:
            return proposal["new"]

    @traitlets.observe("throttle")
    def _clip_throttle(self, proposal):
        """clips the throttle if it exceeds the range [-1.0,1.0]
        Parameters:
        ----------
        proposal : traitlets
            Proposed value for the throttle
        Returns:
        --------
        traitlets
            Validated value for throttle
        """
        if proposal["new"] > 1.0:
            return 1.0
        elif proposal["new"] < -1.0:
            return -1.0
        else:
            return proposal["new"]

    # @traitlets.validate('thr_scale')
    # def _clip_throttle(self, proposal):
    #     """clips the thr_scale if it exceeds the range [0.0,3.0]
    #     Parameters:
    #     ----------
    #     proposal : traitlets
    #         Proposed value for the thr_scale
    #     Returns:
    #     --------
    #     traitlets
    #         Validated value for thr_scale
    #     """
    #     if proposal['value'] > 3.0:
    #         return 3.0
    #     elif proposal['value'] < -0.1:
    #         return 0.0
    #     else:
    #         return proposal['value']


class OpenBotRacer(Racecar):
    """OpenBotRacer Class -> inherits from Racecar and implements the publishing for steering and throttle,
    provides abstraction so every function that needs to control the car can use socket to publish control commands
    Attributes:
    -----------
    autodrive_agent : bool
        Shows where publishing is coming from (joystick or autodrive)
    """

    CONTROL_CMD = ["switch_on", "record", "autodrive", "turn", "reverse"]

    def __init__(self, socket, autodrive_agent=False, *args, **kwargs):
        """
        Default init class
        Args:
            autodrive_agent (bool): Indicate whether the agent is in autodrive mode
        """
        super(OpenBotRacer, self).__init__(*args, **kwargs)
        self.autodrive_agent = autodrive_agent
        self.socket = socket

    def publish_control(self, left, right):
        self.socket.send("{{driveCmd: {{l:{l}, r:{r} }} }}\n".format(l=left, r=right))

    @traitlets.observe("steering")
    def _on_steering(self, change):
        l = (self.throttle + change["new"]) / 2
        r = (self.throttle - change["new"]) / 2
        self.publish_control(l, r)
        print("Steering:", change["new"])

    @traitlets.observe("throttle")
    def _on_throttle(self, change):
        l = (change["new"] + self.steering) / 2
        r = (change["new"] - self.steering) / 2
        self.publish_control(l, r)
        print("Throttle:", change["new"])
